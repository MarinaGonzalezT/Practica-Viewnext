package com.viewnext.kotlinmvvm.data_retrofit.repository

import com.viewnext.kotlinmvvm.data_retrofit.apiService.FacturasApiService
import com.viewnext.kotlinmvvm.domain.FacturasResponse

interface FacturasRepository {
    suspend fun getFacturas(): FacturasResponse
}

class NetworkFacturasRepository(
    private val facturasApiService: FacturasApiService
) : FacturasRepository {
    override suspend fun getFacturas(): FacturasResponse = facturasApiService.getFacturas()
}