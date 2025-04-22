package com.viewnext.kotlinmvvm.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.viewnext.kotlinmvvm.domain.Factura

@Database(entities = [Factura::class], version = 1, exportSchema = false)
abstract class InventoryDataBase : RoomDatabase() {
    abstract fun facturaDao(): FacturaDao
    companion object {
        @Volatile
        private var Instance: InventoryDataBase? = null

        fun getDatabase(context: Context): InventoryDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDataBase::class.java, "factura_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}