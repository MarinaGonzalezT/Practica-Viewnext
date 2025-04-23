package com.viewnext.kotlinmvvm.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.viewnext.kotlinmvvm.domain.Factura
import kotlinx.coroutines.flow.Flow

@Dao
interface FacturaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(facturas: List<Factura>)

    @Query("SELECT * from facturas")
    fun getAllFacturas(): Flow<List<Factura>>

    @Query("DELETE FROM facturas")
    suspend fun deleteAll()
}