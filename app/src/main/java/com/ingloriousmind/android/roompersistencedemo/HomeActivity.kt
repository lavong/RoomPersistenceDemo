package com.ingloriousmind.android.roompersistencedemo

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ingloriousmind.android.roompersistencedemo.persistence.AppDatabase
import com.ingloriousmind.android.roompersistencedemo.product.Product
import com.ingloriousmind.android.roompersistencedemo.product.ProductAdapter
import com.ingloriousmind.android.roompersistencedemo.product.ProductAdapter.ClickHandler
import com.ingloriousmind.android.roompersistencedemo.product.ProductDao
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeActivity : AppCompatActivity() {

    private lateinit var itemCount: TextView
    private lateinit var total: TextView
    private lateinit var name: EditText
    private lateinit var amount: EditText
    private lateinit var recycler: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var insertButton: Button
    private lateinit var dao: ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()

        // for demo brevity, usually one wouldn't do business logic in here
        dao = Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.DB_NAME).build().productDao()

        dao.getAll().doOnNext {
            Log.d("abc", it.map { it.name }.joinToString(", "))
        }.observeOn(AndroidSchedulers.mainThread()).subscribe {
            productAdapter.setProducts(it)
            itemCount.text = "item count ${it.count()}"
            total.text = "total amount: ${it.map { it.amount }.sum()}"
        }

    }

    fun validateInput(): Boolean {
        val hasName = name.text.isNotBlank()
        val hasAmount = amount.text.isNotBlank()
        if (!hasName) {
            name.error = "name?"
            name.requestFocus()
        }
        if (!hasAmount) {
            amount.error = "amount?"
            amount.requestFocus()
        }
        return hasName && hasAmount
    }

    private fun initViews() {
        itemCount = findViewById(R.id.item_count) as TextView
        total = findViewById(R.id.total) as TextView
        name = findViewById(R.id.name) as EditText
        amount = findViewById(R.id.amount) as EditText
        insertButton = findViewById(R.id.insert_button) as Button
        recycler = findViewById(R.id.recycler) as RecyclerView
        productAdapter = ProductAdapter(object : ClickHandler {
            override fun onProductClicked(product: Product, position: Int) {
                Single.fromCallable {
                    dao.delete(product)
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSuccess {
                    productAdapter.notifyItemRemoved(position)
                }.subscribe()
            }
        })
        recycler.adapter = productAdapter
        recycler.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        recycler.itemAnimator = DefaultItemAnimator()

        insertButton.setOnClickListener {
            if (validateInput()) {
                Single.fromCallable {
                    dao.insert(Product(name = name.text.toString(), amount = amount.text.toString().toLong()))
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSuccess {
                    name.setText("")
                    amount.setText("")
                }.subscribe()
            }
        }
    }
}
