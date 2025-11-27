package com.example.lab6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mechanism")
data class MechanismEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val warehouseId: Int?,
    val name: String,
    val manufacturer: String,
    val year: Int,
    val price: Double
)