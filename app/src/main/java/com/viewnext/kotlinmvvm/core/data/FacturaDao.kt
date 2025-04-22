package com.viewnext.kotlinmvvm.core.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.viewnext.kotlinmvvm.domain.Factura
import kotlinx.coroutines.flow.Flow

@Dao
interface FacturaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Factura)

    @Query("SELECT * from facturas")
    fun getAllFacturas(): Flow<List<Factura>>
}