package com.viewnext.kotlinmvvm.domain.model

data class Filtros(
    val importeMin: Float = 0f,
    val importeMax: Float = 300f,
    val fechaDesde: Long? = null,
    val fechaHasta: Long? = null,
    val estados: List<String> = emptyList()
)