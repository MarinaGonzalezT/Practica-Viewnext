package com.viewnext.kotlinmvvm.core.ui

import com.viewnext.kotlinmvvm.domain.model.Factura

sealed interface FacturasUiState {
    data class Succes(val facturas: List<Factura>) : FacturasUiState
    data class Error(val mensaje: String) : FacturasUiState
    object Loading : FacturasUiState
}