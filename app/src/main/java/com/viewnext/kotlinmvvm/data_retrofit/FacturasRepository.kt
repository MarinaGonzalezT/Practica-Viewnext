package com.viewnext.kotlinmvvm.data_retrofit

import com.viewnext.kotlinmvvm.domain.FacturasResponse

interface FacturasRepository {
    suspend fun getFacturas(): FacturasResponse
}

class NetworkFacturasRepository(
    private val appApiService: AppApiService
) : FacturasRepository {
    override suspend fun getFacturas(): FacturasResponse = appApiService.getFacturas()
}