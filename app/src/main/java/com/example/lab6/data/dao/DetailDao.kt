package com.example.lab6.data.dao

import com.example.lab6.data.entities.DetailEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface DetailDao {
    @Insert
    suspend fun insert(detail: DetailEntity)

    @Update
    suspend fun update(detail: DetailEntity)

    @Delete
    suspend fun delete(detail: DetailEntity)

    @Query("SELECT * FROM details")
    suspend fun getAll(): List<DetailEntity>
}