package com.viewnext.kotlinmvvm.data_retrofit.apiService

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.viewnext.kotlinmvvm.domain.FacturasResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://e7cd6b3c-bd3f-482e-b78a-1b5920cef8b0.mock.pstmn.io/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface FacturasApiService {
    @GET("facturas")
    @Mock
    @MockResponse(body = "facturas.json")
    suspend fun getFacturas() : FacturasResponse
}