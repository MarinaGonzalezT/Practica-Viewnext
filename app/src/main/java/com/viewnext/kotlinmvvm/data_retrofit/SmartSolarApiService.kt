package com.viewnext.kotlinmvvm.data_retrofit

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.viewnext.kotlinmvvm.domain.model.Detalles
import retrofit2.http.GET

interface SmartSolarApiService {
    @GET("detalles")
    @Mock
    @MockResponse(body = "detalles.json")
    suspend fun getDetalles(): Detalles
}