package com.example.lab6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assembly")
data class AssemblyEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var mechanismId: Int?,
    val warehouseId: Int?,
    val name: String,
    val manufacturer: String,
    val year: Int,
    val price: Double
)