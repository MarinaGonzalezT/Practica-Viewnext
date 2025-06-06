package com.viewnext.kotlinmvvm.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viewnext.kotlinmvvm.domain.model.Filtros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FiltrosViewModel(
    private val facturasViewModel: FacturasViewModel
) : ViewModel() {
    private val _filtros = MutableStateFlow(facturasViewModel.obtenerFiltrosActuales())
    val filtros: StateFlow<Filtros> = _filtros

    fun actualizarFiltros(nuevos: Filtros) {
        _filtros.value = nuevos
        facturasViewModel.aplicarFiltros(nuevos)
    }

    fun eliminarFiltros() {
        _filtros.value = Filtros()
        facturasViewModel.aplicarFiltros(Filtros())
    }

    fun actualizarFechaDesde(nuevaFecha: Long?) {
        _filtros.value = _filtros.value.copy(fechaDesde = nuevaFecha)
    }

    fun actualizarFechaHasta(nuevaFecha: Long?) {
        _filtros.value = _filtros.value.copy(fechaHasta = nuevaFecha)
    }

    companion object {
        fun provideFactory(facturasViewModel: FacturasViewModel): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FiltrosViewModel(facturasViewModel) as T
                }
            }
    }
}