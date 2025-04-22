package com.viewnext.kotlinmvvm.data_retrofit.data

import com.viewnext.kotlinmvvm.data_retrofit.FacturasApiService
import com.viewnext.kotlinmvvm.domain.FacturasResponse

interface FacturasRepository {
    suspend fun getFacturas(): FacturasResponse
}

class NetworkFacturasRepository(
    private val facturasApiService: FacturasApiService
) : FacturasRepository {
    override suspend fun getFacturas(): FacturasResponse = facturasApiService.getFacturas()
}