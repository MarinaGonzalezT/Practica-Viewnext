package com.viewnext.kotlinmvvm.domain

data class FacturasResponse(
    val numFacturas: Int,
    val facturas: List<Factura>
)