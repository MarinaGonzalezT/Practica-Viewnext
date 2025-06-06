package com.viewnext.kotlinmvvm.domain.model

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

    val fecha: String,

    @SerialName("kwhTotal")
    val kwh_totales: Double = 0.0,

    @SerialName("kwhHoraPunta")
    val kwh_horaPunta: Double = 0.0
)