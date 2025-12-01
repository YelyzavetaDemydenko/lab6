package com.example.lab6.data.repository

import com.example.lab6.data.dao.AssemblyDao
import com.example.lab6.data.entities.AssemblyEntity


class OfflineAssemblyRepository(private val dao: AssemblyDao) : AssemblyRepository {
    override suspend fun getAllAssemblies(): List<AssemblyEntity> = dao.getAll()

    override suspend fun getAssembliesByWarehouse(warehouseId: Int): List<AssemblyEntity> =
        dao.getByWarehouse(warehouseId)

    override suspend fun getAssembliesByMechanism(mechanismId: Int): List<AssemblyEntity> =
        dao.getByMechanism(mechanismId)

    override suspend fun insertAssembly(assembly: AssemblyEntity): Long =
        dao.insert(assembly)

    override suspend fun updateAssembly(assembly: AssemblyEntity) = dao.update(assembly)

    override suspend fun deleteAssembly(assembly: AssemblyEntity) = dao.delete(assembly)
}