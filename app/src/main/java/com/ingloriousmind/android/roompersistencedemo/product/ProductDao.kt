package com.ingloriousmind.android.roompersistencedemo.product

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAll(): Flowable<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product): Unit

    @Delete
    fun delete(product: Product): Unit

}