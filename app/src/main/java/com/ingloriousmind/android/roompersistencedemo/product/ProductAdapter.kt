package com.ingloriousmind.android.roompersistencedemo.product

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ingloriousmind.android.roompersistencedemo.R

class ProductAdapter(val clickHandler: ClickHandler) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    var items: List<Product> = ArrayList()

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ProductHolder, pos: Int) {
        val product = items.get(pos)
        holder.name.text = product.name
        holder.amount.text = product.amount.toString()
        holder.itemView.setOnClickListener {
            clickHandler.onProductClicked(product, pos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_product, parent, false)
        return ProductHolder(view)
    }

    fun setProducts(products: List<Product>) {
        items = products
        notifyDataSetChanged()
    }

    inner class ProductHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val amount: TextView

        init {
            name = view.findViewById(R.id.product_name) as TextView
            amount = view.findViewById(R.id.product_amount) as TextView
        }
    }

    interface ClickHandler {
        fun onProductClicked(product: Product, position: Int)
    }

}