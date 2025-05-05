package com.viewnext.kotlinmvvm.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viewnext.kotlinmvvm.domain.model.Filtros
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FiltrosViewModel(
    private val facturasViewModel: FacturasViewModel
) : ViewModel() {
    private val _filtros = MutableStateFlow(facturasViewModel.obtenerFiltrosActuales())
    val filtros: StateFlow<Filtros> = _filtros

    private val _mensajeError = MutableSharedFlow<String>()
    val mensajeError: SharedFlow<String> = _mensajeError

    fun actualizarFiltros(nuevos: Filtros) {
        _filtros.value = nuevos
        facturasViewModel.aplicarFiltros(nuevos)
    }

    fun eliminarFiltros() {
        _filtros.value = Filtros()
        facturasViewModel.aplicarFiltros(Filtros())
    }

    fun actualizarFechaDesde(nuevaFecha: Long?) {
        val diaActual = System.currentTimeMillis()
        val hasta = _filtros.value.fechaHasta

        if(nuevaFecha != null && nuevaFecha > diaActual) {
            lanzarError("No puedes seleccionar una fecha posterior a hoy")
            return
        }

        if(hasta != null && nuevaFecha != null && nuevaFecha > hasta) {
            lanzarError("La fecha 'Desde' debe ser anterior a la fecha 'Hasta'")
            return
        }

        _filtros.value = _filtros.value.copy(fechaDesde = nuevaFecha)
    }

    fun actualizarFechaHasta(nuevaFecha: Long?) {
        val diaActual = System.currentTimeMillis()
        val desde = _filtros.value.fechaDesde

        if(nuevaFecha != null && nuevaFecha > diaActual) {
            lanzarError("No puedes seleccionar una fecha posterior a hoy")
            return
        }

        if(desde != null && nuevaFecha != null && nuevaFecha < desde) {
            lanzarError("La fecha 'Hasta' debe ser posterior a la fecha 'Desde'")
            return
        }

        _filtros.value = _filtros.value.copy(fechaHasta = nuevaFecha)
    }

    private fun lanzarError(mensaje: String) {
        viewModelScope.launch {
            _filtros.value = _filtros.value.copy(
                fechaDesde = null,
                fechaHasta = null
            )
            _mensajeError.emit(mensaje)
        }
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