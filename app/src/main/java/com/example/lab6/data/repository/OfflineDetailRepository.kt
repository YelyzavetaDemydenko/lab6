package com.example.lab6.data.repository

import com.example.lab6.data.dao.DetailDao
import com.example.lab6.data.entities.DetailEntity
import kotlinx.coroutines.flow.Flow

class OfflineDetailRepository(private val dao: DetailDao) : DetailRepository {

    override fun getAllDetails(): Flow<List<DetailEntity>> =
        dao.getAll()

    override fun getDetailsByWarehouse(warehouseId: Int): Flow<List<DetailEntity>> =
        dao.getByWarehouse(warehouseId)

    override fun getDetailsByAssembly(assemblyId: Int): Flow<List<DetailEntity>> =
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
