package com.example.lab6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warehouse")
data class WarehouseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val password: String,
    val address: String? = null

)