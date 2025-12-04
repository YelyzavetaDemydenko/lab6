package com.example.lab6.data.dao

import com.example.lab6.data.entities.DetailEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao {
    @Insert
    suspend fun insert(detail: DetailEntity)

    @Update
    suspend fun update(detail: DetailEntity)

    @Delete
    suspend fun delete(detail: DetailEntity)

    @Query("SELECT * FROM details")
    fun getAll(): Flow<List<DetailEntity>>

    @Query("SELECT * FROM details WHERE warehouseId = :warehouseId")
    fun getByWarehouse(warehouseId: Int): Flow<List<DetailEntity>>

    @Query("SELECT * FROM details WHERE assemblyId = :assemblyId")
    fun getByAssembly(assemblyId: Int): Flow<List<DetailEntity>>

    @Query("UPDATE details SET assemblyId = :assemblyId WHERE id = :detailId")
    suspend fun updateAssemblyIdForDetail(detailId: Int, assemblyId: Int?)

    @Query("DELETE FROM details WHERE assemblyId = :assemblyId")
    suspend fun deleteDetailsByAssemblyId(assemblyId: Int)
}