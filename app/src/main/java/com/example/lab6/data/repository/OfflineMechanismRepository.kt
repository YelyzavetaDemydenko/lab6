package com.example.lab6.data.repository

import com.example.lab6.data.dao.MechanismDao
import com.example.lab6.data.entities.MechanismEntity

class OfflineMechanismRepository(private val dao: MechanismDao) : MechanismRepository {

    override suspend fun getAllMechanisms(): List<MechanismEntity> {
        return dao.getAll()
    }

    override suspend fun getMechanismsByWarehouse(warehouseId: Int): List<MechanismEntity> {
        return dao.getByWarehouse(warehouseId)
    }

    override suspend fun insertMechanism(mechanism: MechanismEntity) {
        dao.insert(mechanism)
    }

    override suspend fun updateMechanism(mechanism: MechanismEntity) {
        dao.update(mechanism)
    }

    override suspend fun deleteMechanism(mechanism: MechanismEntity) {
        dao.delete(mechanism)
    }
}
