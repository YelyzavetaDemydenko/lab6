package com.example.lab6.data.repository

import com.example.lab6.data.entities.DetailEntity
import kotlinx.coroutines.flow.Flow


interface DetailRepository {
    fun getAllDetails(): Flow<List<DetailEntity>>
    fun getDetailsByWarehouse(warehouseId: Int): Flow<List<DetailEntity>>
    fun getDetailsByAssembly(assemblyId: Int): Flow<List<DetailEntity>>
    suspend fun insertDetail(detail: DetailEntity)
    suspend fun updateDetail(detail: DetailEntity)
    suspend fun deleteDetail(detail: DetailEntity)
    suspend fun updateAssemblyIdForDetail(detailId: Int, assemblyId: Int?)

    suspend fun deleteDetailsByAssemblyId(assemblyId: Int)
}