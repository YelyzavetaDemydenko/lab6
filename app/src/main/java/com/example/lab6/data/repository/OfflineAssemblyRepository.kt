package com.example.lab6.data.repository

import com.example.lab6.data.dao.AssemblyDao
import com.example.lab6.data.entities.AssemblyEntity
import kotlinx.coroutines.flow.Flow

class OfflineAssemblyRepository(private val dao: AssemblyDao) : AssemblyRepository {

    override fun getAllAssemblies(): Flow<List<AssemblyEntity>> = dao.getAll()

    override fun getAssembliesByWarehouse(warehouseId: Int): Flow<List<AssemblyEntity>> =
        dao.getByWarehouse(warehouseId)

    override fun getAssembliesByMechanism(mechanismId: Int): Flow<List<AssemblyEntity>> =
        dao.getByMechanism(mechanismId)

    override suspend fun insertAssembly(assembly: AssemblyEntity): Long {
        return dao.insert(assembly)
    }

    override suspend fun updateAssembly(assembly: AssemblyEntity) = dao.update(assembly)

    override suspend fun deleteAssembly(assembly: AssemblyEntity) = dao.delete(assembly)

    override suspend fun deleteAssembliesByMechanismId(mechanismId: Int) =
        dao.deleteAssembliesByMechanismId(mechanismId)
}
