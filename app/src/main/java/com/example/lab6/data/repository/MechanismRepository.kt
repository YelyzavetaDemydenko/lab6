package com.example.lab6.data.repository

import com.example.lab6.data.entities.MechanismEntity
import kotlinx.coroutines.flow.Flow

interface MechanismRepository {
    fun getAllMechanisms(): Flow<List<MechanismEntity>>
    fun getMechanismsByWarehouse(warehouseId: Int): Flow<List<MechanismEntity>>
    suspend fun insertMechanism(mechanism: MechanismEntity): Long
    suspend fun updateMechanism(mechanism: MechanismEntity)
    suspend fun deleteMechanism(mechanism: MechanismEntity)
}
