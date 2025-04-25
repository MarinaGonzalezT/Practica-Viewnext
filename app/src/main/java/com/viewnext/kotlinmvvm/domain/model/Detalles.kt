package com.viewnext.kotlinmvvm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Detalles(
    val cau: String,
    val estado: String,
    val tipo: String,
    val compensacion: String,
    val potencia: String
)