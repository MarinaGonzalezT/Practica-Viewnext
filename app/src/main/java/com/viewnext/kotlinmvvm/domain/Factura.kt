package com.viewnext.kotlinmvvm.domain

data class Factura(
    val fecha: String,
    val estado: String,
    val importe: Double
)

val facturasPrueba = listOf(
    Factura("31 Ago 2020", "Pendiente de pago", 54.56),
    Factura("31 Jul 2020", "Pendiente de pago", 67.54),
    Factura("22 Jun 2020", "Pendiente de pago", 56.38),
    Factura("31 May 2020", "Pagada", 57.38),
    Factura("22 Abr 2020", "Pagada", 65.23),
    Factura("20 Mar 2020", "Pagada", 74.54),
    Factura("22 Feb 2020", "Pagada", 67.64)
)

val facturaPrueba1 = Factura("22 Jun 2020", "Pendiente de pago", 56.38)
val facturaPrueba2 = Factura("22 Jun 2020", "Pago realizado", 56.38)