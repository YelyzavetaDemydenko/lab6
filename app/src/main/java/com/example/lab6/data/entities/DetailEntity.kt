package com.example.lab6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details")
data class DetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var assemblyId: Int?,
    val warehouseId: Int?,
    var name: String,
    var manufacturer: String,
    val year: Int,
    val price: Double,
    var material: String
)