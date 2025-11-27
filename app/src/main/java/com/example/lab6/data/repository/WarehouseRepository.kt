package com.example.lab6.data.repository

import com.example.lab6.data.entities.WarehouseEntity

interface WarehouseRepository {
    suspend fun getAllWarehouses(): List<WarehouseEntity>
    suspend fun getWarehouseByLogin(name: String, password: String): WarehouseEntity?
    suspend fun insertWarehouse(warehouse: WarehouseEntity)
    suspend fun updateWarehouse(warehouse: WarehouseEntity)
    suspend fun deleteWarehouse(warehouse: WarehouseEntity)
}