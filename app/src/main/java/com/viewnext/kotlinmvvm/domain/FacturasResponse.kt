package com.viewnext.kotlinmvvm.domain

import kotlinx.serialization.Serializable

@Serializable
data class FacturasResponse(
    val numFacturas: Int,
    val facturas: List<Factura>
)