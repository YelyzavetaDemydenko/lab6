package com.example.lab6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details")
data class DetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var assemblyId: Int?,
    val warehouseId: Int?,
    val name: String,
    val manufacturer: String,
    val year: Int,
    val price: Double,
    val material: String
)