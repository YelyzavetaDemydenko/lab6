package com.example.lab6.data.dao

import com.example.lab6.data.entities.DetailEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface DetailDao {
    @Insert
    suspend fun insert(detail: DetailEntity)

    @Update
    suspend fun update(detail: DetailEntity)

    @Delete
    suspend fun delete(detail: DetailEntity)

    @Query("SELECT * FROM details")
    suspend fun getAll(): List<DetailEntity>

    @Query("SELECT * FROM details WHERE warehouseId = :warehouseId")
    suspend fun getByWarehouse(warehouseId: Int): List<DetailEntity>

    @Query("SELECT * FROM details WHERE assemblyId = :assemblyId")
    suspend fun getByAssembly(assemblyId: Int): List<DetailEntity>

    @Query("UPDATE details SET assemblyId = :assemblyId WHERE id = :detailId")
    suspend fun updateAssemblyIdForDetail(detailId: Int, assemblyId: Int?)
}