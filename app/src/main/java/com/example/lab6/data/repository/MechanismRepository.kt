package com.example.lab6.data.repository

import com.example.lab6.data.entities.MechanismEntity

interface MechanismRepository {
    suspend fun getAllMechanisms(): List<MechanismEntity>
    suspend fun getMechanismsByWarehouse(warehouseId: Int): List<MechanismEntity>
    suspend fun insertMechanism(mechanism: MechanismEntity)
    suspend fun updateMechanism(mechanism: MechanismEntity)
    suspend fun deleteMechanism(mechanism: MechanismEntity)
}