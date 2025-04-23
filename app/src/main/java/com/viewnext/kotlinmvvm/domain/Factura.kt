package com.viewnext.kotlinmvvm.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "facturas")
@Serializable
data class Factura(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerialName("descEstado")
    val estado: String,

    @SerialName("importeOrdenacion")
    val importe: Double,

    val fecha: String
)

val facturaPrueba1 = Factura(0, "Pendiente de pago", 56.38, "22 Jun 2020")
val facturaPrueba2 = Factura(1, "Pago realizado", 56.38, "22 Jun 2020")