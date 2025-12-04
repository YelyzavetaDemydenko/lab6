package com.example.lab6.data.repository

import com.example.lab6.data.dao.MechanismDao
import com.example.lab6.data.entities.MechanismEntity
import kotlinx.coroutines.flow.Flow

class OfflineMechanismRepository(private val dao: MechanismDao) : MechanismRepository {

    override fun getAllMechanisms(): Flow<List<MechanismEntity>> = dao.getAll()

    override fun getMechanismsByWarehouse(warehouseId: Int): Flow<List<MechanismEntity>> =
        dao.getByWarehouse(warehouseId)

    override suspend fun insertMechanism(mechanism: MechanismEntity): Long {
        return dao.insert(mechanism)
    }

    override suspend fun updateMechanism(mechanism: MechanismEntity) {
        dao.update(mechanism)
    }

    override suspend fun deleteMechanism(mechanism: MechanismEntity) {
        dao.delete(mechanism)
    }
}