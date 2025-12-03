package com.example.lab6.data.repository

import com.example.lab6.data.entities.DetailEntity


interface DetailRepository {
    suspend fun getAllDetails(): List<DetailEntity>
    suspend fun getDetailsByWarehouse(warehouseId: Int): List<DetailEntity>
    suspend fun getDetailsByAssembly(assemblyId: Int): List<DetailEntity>
    suspend fun insertDetail(detail: DetailEntity)
    suspend fun updateDetail(detail: DetailEntity)
    suspend fun deleteDetail(detail: DetailEntity)
    suspend fun updateAssemblyIdForDetail(detailId: Int, assemblyId: Int?)

    suspend fun deleteDetailsByAssemblyId(assemblyId: Int)
}