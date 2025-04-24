package com.viewnext.kotlinmvvm.core.data.repository

import com.viewnext.kotlinmvvm.core.data.database.FacturaDao
import com.viewnext.kotlinmvvm.domain.model.Factura
import kotlinx.coroutines.flow.Flow

interface RoomFacturasRepository {
    fun getAllFacturasStream(): Flow<List<Factura>>
    suspend fun refreshFacturasFromNetwork(nuevasFacturas: List<Factura>)
}

class OfflineFacturasRepository(private val facturaDao: FacturaDao) : RoomFacturasRepository {
    override fun getAllFacturasStream(): Flow<List<Factura>> = facturaDao.getAllFacturas()

    override suspend fun refreshFacturasFromNetwork(nuevasFacturas: List<Factura>) {
        facturaDao.deleteAll()
        facturaDao.insertAll(nuevasFacturas)
    }
}