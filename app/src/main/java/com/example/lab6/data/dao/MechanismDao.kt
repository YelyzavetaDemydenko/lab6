package com.example.lab6.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.example.lab6.data.entities.MechanismEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MechanismDao {
    @Insert
    suspend fun insert(mechanism: MechanismEntity): Long

    @Update
    suspend fun update(mechanism: MechanismEntity)

    @Delete
    suspend fun delete(mechanism: MechanismEntity)

    @Query("SELECT * FROM mechanism")
    fun getAll(): Flow<List<MechanismEntity>>

    @Query("SELECT * FROM mechanism WHERE warehouseId = :warehouseId")
    fun getByWarehouse(warehouseId: Int): Flow<List<MechanismEntity>>
}
