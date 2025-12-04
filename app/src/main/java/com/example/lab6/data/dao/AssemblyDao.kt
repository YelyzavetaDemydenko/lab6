package com.example.lab6.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.lab6.data.entities.AssemblyEntity

@Dao
interface AssemblyDao {
    @Insert
    suspend fun insert(assembly: AssemblyEntity): Long

    @Update
    suspend fun update(assembly: AssemblyEntity)

    @Delete
    suspend fun delete(assembly: AssemblyEntity)

    @Query("SELECT * FROM assembly")
    fun getAll(): Flow<List<AssemblyEntity>>

    @Query("SELECT * FROM assembly WHERE warehouseId = :warehouseId")
    fun getByWarehouse(warehouseId: Int): Flow<List<AssemblyEntity>>

    @Query("SELECT * FROM assembly WHERE mechanismId = :mechanismId")
    fun getByMechanism(mechanismId: Int): Flow<List<AssemblyEntity>>

    @Query("DELETE FROM assembly WHERE mechanismId = :mechanismId")
    suspend fun deleteAssembliesByMechanismId(mechanismId: Int)
}
