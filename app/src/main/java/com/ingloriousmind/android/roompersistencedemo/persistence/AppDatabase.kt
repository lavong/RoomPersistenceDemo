package com.ingloriousmind.android.roompersistencedemo.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.ingloriousmind.android.roompersistencedemo.product.Product
import com.ingloriousmind.android.roompersistencedemo.product.ProductDao

@Database(entities = arrayOf(Product::class), version = AppDatabase.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "app.db"
        const val DB_VERSION = 1
    }

    abstract fun productDao(): ProductDao

}