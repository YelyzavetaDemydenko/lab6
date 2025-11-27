package com.example.lab6.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

import com.example.lab6.data.entities.WarehouseEntity

@Dao
interface WarehouseDao {
    @Insert
    suspend fun insert(warehouse: WarehouseEntity)

    @Update
    suspend fun update(warehouse: WarehouseEntity)

    @Delete
    suspend fun delete(warehouse: WarehouseEntity)

    @Query("SELECT * FROM warehouse")
    suspend fun getAll(): List<WarehouseEntity>

    @Query("SELECT * FROM warehouse WHERE name = :name AND password = :password LIMIT 1")
    suspend fun getByLogin(name: String, password: String): WarehouseEntity?
}