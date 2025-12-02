package com.example.lab6.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

import com.example.lab6.data.entities.MechanismEntity

@Dao
interface MechanismDao {
    @Insert
    suspend fun insert(mechanism: MechanismEntity) : Long

    @Update
    suspend fun update(mechanism: MechanismEntity)

    @Delete
    suspend fun delete(mechanism: MechanismEntity)

    @Query("SELECT * FROM mechanism")
    suspend fun getAll(): List<MechanismEntity>

    @Query("SELECT * FROM mechanism WHERE warehouseId = :warehouseId")
    suspend fun getByWarehouse(warehouseId: Int): List<MechanismEntity>
}
