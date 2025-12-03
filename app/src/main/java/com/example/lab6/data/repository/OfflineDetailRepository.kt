package com.example.lab6.data.repository

import com.example.lab6.data.dao.DetailDao
import com.example.lab6.data.entities.DetailEntity

class OfflineDetailRepository(private val dao: DetailDao) : DetailRepository {

    override suspend fun getAllDetails(): List<DetailEntity> {
        return dao.getAll()
    }

    override suspend fun getDetailsByWarehouse(warehouseId: Int) =
        dao.getByWarehouse(warehouseId)

    override suspend fun getDetailsByAssembly(assemblyId: Int) =
        dao.getByAssembly(assemblyId)


    override suspend fun insertDetail(detail: DetailEntity) {
        dao.insert(detail)
    }

    override suspend fun updateDetail(detail: DetailEntity) {
        dao.update(detail)
    }

    override suspend fun deleteDetail(detail: DetailEntity) {
        dao.delete(detail)
    }

    override suspend fun updateAssemblyIdForDetail(detailId: Int, assemblyId: Int?) {
        dao.updateAssemblyIdForDetail(detailId, assemblyId)
    }

    override suspend fun deleteDetailsByAssemblyId(assemblyId: Int) =
        dao.deleteDetailsByAssemblyId(assemblyId)
}
