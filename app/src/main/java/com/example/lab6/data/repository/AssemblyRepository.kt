package com.example.lab6.data.repository

import com.example.lab6.data.entities.AssemblyEntity
import kotlinx.coroutines.flow.Flow

interface AssemblyRepository {
    fun getAllAssemblies(): Flow<List<AssemblyEntity>>
    fun getAssembliesByWarehouse(warehouseId: Int): Flow<List<AssemblyEntity>>
    fun getAssembliesByMechanism(mechanismId: Int): Flow<List<AssemblyEntity>>

    suspend fun insertAssembly(assembly: AssemblyEntity): Long
    suspend fun updateAssembly(assembly: AssemblyEntity)
    suspend fun deleteAssembly(assembly: AssemblyEntity)
    suspend fun deleteAssembliesByMechanismId(mechanismId: Int)
}
