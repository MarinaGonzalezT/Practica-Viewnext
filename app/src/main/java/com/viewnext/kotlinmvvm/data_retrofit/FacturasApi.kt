package com.viewnext.kotlinmvvm.data_retrofit

import com.viewnext.kotlinmvvm.ui.Factura
import retrofit2.http.GET

interface FacturasApi {
    @GET("Facturas")
    suspend fun getFacturas(): List<Factura>
}

data class FacturasResponse(
    val numFacturas: Int,
    val facturas: List<Factura>
)