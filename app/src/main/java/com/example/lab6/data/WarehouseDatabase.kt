package com.example.lab6.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab6.data.dao.AssemblyDao
import com.example.lab6.data.dao.DetailDao
import com.example.lab6.data.dao.MechanismDao
import com.example.lab6.data.dao.WarehouseDao
import com.example.lab6.data.entities.DetailEntity
import com.example.lab6.data.entities.AssemblyEntity
import com.example.lab6.data.entities.MechanismEntity
import com.example.lab6.data.entities.WarehouseEntity

@Database(
    entities = [
        DetailEntity::class,
        AssemblyEntity::class,
        MechanismEntity::class,
        WarehouseEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun detailDao(): DetailDao
    abstract fun assemblyDao(): AssemblyDao
    abstract fun mechanismDao(): MechanismDao
    abstract fun warehouseDao(): WarehouseDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
