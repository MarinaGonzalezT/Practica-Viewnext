package com.viewnext.kotlinmvvm.domain

import com.viewnext.kotlinmvvm.domain.model.Factura
import kotlinx.serialization.Serializable

@Serializable
data class FacturasResponse(
    val numFacturas: Int,
    val facturas: List<Factura>
)