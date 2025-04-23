package com.viewnext.kotlinmvvm.domain

data class Filtros(
    val importeMin: Float? = null,
    val importeMax: Float? = null,
    val fechaDesde: Long? = null,
    val fechaHasta: Long? = null,
    val estados: List<String> = emptyList()
)