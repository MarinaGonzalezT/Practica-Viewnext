package com.viewnext.kotlinmvvm.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viewnext.kotlinmvvm.domain.model.Factura

@Database(entities = [Factura::class], version = 1, exportSchema = false)
abstract class FacturaDatabase : RoomDatabase() {
    abstract fun facturaDao(): FacturaDao
}