package com.ingloriousmind.android.roompersistencedemo.product

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "products")
data class Product constructor(
        @PrimaryKey(autoGenerate = true) var uid: Int = 0,
        var name: String = "",
        var amount: Long = 0L
) {

}