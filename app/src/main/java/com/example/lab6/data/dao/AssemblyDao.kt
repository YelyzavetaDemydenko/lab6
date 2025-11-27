package com.example.lab6.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

import com.example.lab6.data.entities.AssemblyEntity


@Dao
interface AssemblyDao {
    @Insert
    suspend fun insert(assembly: AssemblyEntity)

    @Update
    suspend fun update(assembly: AssemblyEntity)

    @Delete
    suspend fun delete(assembly: AssemblyEntity)

    @Query("SELECT * FROM assembly")
    suspend fun getAll(): List<AssemblyEntity>

    @Query("SELECT * FROM assembly WHERE warehouseId = :warehouseId")
    suspend fun getByWarehouse(warehouseId: Int): List<AssemblyEntity>

    @Query("SELECT * FROM assembly WHERE mechanismId = :mechanismId")
    suspend fun getByMechanism(mechanismId: Int): List<AssemblyEntity>
}