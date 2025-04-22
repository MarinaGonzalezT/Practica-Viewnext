package com.viewnext.kotlinmvvm.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Factura(
    @SerialName("descEstado")
    val estado: String,

    @SerialName("importeOrdenacion")
    val importe: Double,

    val fecha: String
)

val facturaPrueba1 = Factura("Pendiente de pago", 56.38, "22 Jun 2020")
val facturaPrueba2 = Factura("Pago realizado", 56.38, "22 Jun 2020")