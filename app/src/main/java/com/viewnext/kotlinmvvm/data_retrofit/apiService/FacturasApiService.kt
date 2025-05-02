package com.viewnext.kotlinmvvm.data_retrofit.apiService

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.viewnext.kotlinmvvm.domain.FacturasResponse
import retrofit2.http.GET

interface FacturasApiService {
    @GET("facturas")
    @Mock
    @MockResponse(body = "facturas.json")
    suspend fun getFacturas() : FacturasResponse
}