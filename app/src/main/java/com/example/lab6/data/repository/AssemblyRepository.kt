package com.example.lab6.data.repository

import com.example.lab6.data.entities.AssemblyEntity


interface AssemblyRepository {
    suspend fun getAllAssemblies(): List<AssemblyEntity>
    suspend fun getAssembliesByWarehouse(warehouseId: Int): List<AssemblyEntity>
    suspend fun getAssembliesByMechanism(mechanismId: Int): List<AssemblyEntity>
    suspend fun insertAssembly(assembly: AssemblyEntity) : Long
    suspend fun updateAssembly(assembly: AssemblyEntity)
    suspend fun deleteAssembly(assembly: AssemblyEntity)
}