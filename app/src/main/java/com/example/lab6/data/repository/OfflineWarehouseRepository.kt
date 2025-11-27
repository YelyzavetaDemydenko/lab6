package com.example.lab6.data.repository

import com.example.lab6.data.dao.WarehouseDao
import com.example.lab6.data.entities.WarehouseEntity

class OfflineWarehouseRepository(private val dao: WarehouseDao) : WarehouseRepository {

    override suspend fun getAllWarehouses(): List<WarehouseEntity> {
        return dao.getAll()
    }

    override suspend fun getWarehouseByLogin(name: String, password: String): WarehouseEntity? {
        return dao.getByLogin(name, password)
    }

    override suspend fun insertWarehouse(warehouse: WarehouseEntity) {
        dao.insert(warehouse)
    }

    override suspend fun updateWarehouse(warehouse: WarehouseEntity) {
        dao.update(warehouse)
    }

    override suspend fun deleteWarehouse(warehouse: WarehouseEntity) {
        dao.delete(warehouse)
    }
}
