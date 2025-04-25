package com.viewnext.kotlinmvvm.data_retrofit

import com.viewnext.kotlinmvvm.domain.model.Detalles

interface DetallesRepository {
    suspend fun getDetalles(): Detalles
}

class NetworkDetallesRepository(
    private val apiService: SmartSolarApiService
) : DetallesRepository {
    override suspend fun getDetalles(): Detalles = apiService.getDetalles()
}
