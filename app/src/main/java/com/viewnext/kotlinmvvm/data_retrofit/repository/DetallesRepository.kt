package com.viewnext.kotlinmvvm.data_retrofit.repository

import com.viewnext.kotlinmvvm.data_retrofit.apiService.SmartSolarApiService
import com.viewnext.kotlinmvvm.domain.model.Detalles

interface DetallesRepository {
    suspend fun getDetalles(): Detalles
}

class NetworkDetallesRepository(
    private val apiService: SmartSolarApiService
) : DetallesRepository {
    override suspend fun getDetalles(): Detalles = apiService.getDetalles()
}
