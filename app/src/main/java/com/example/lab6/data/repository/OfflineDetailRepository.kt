package com.example.lab6.data.repository

import com.example.lab6.data.dao.DetailDao
import com.example.lab6.data.entities.DetailEntity

class OfflineDetailRepository(private val dao: DetailDao) : DetailRepository {

    override suspend fun getAllDetails(): List<DetailEntity> {
        return dao.getAll()
    }

    override suspend fun getDetailsByWarehouse(warehouseId: Int): List<DetailEntity> {
        return dao.getAll().filter { it.warehouseId == warehouseId }
    }

    override suspend fun getDetailsByAssembly(assemblyId: Int): List<DetailEntity> {
        return dao.getAll().filter { it.assemblyId == assemblyId }
    }

    override suspend fun insertDetail(detail: DetailEntity) {
        dao.insert(detail)
    }

    override suspend fun updateDetail(detail: DetailEntity) {
        dao.update(detail)
    }

    override suspend fun deleteDetail(detail: DetailEntity) {
        dao.delete(detail)
    }
}
