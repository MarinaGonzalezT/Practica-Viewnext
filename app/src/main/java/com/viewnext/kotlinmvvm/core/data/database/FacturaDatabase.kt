package com.viewnext.kotlinmvvm.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.viewnext.kotlinmvvm.domain.Factura

@Database(entities = [Factura::class], version = 1, exportSchema = false)
abstract class FacturaDatabase : RoomDatabase() {
    abstract fun facturaDao(): FacturaDao

    companion object {
        @Volatile
        private var Instance: FacturaDatabase? = null

        fun getDatabase(context: Context): FacturaDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FacturaDatabase::class.java, "factura_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}