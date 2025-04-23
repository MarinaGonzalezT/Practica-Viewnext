package com.viewnext.kotlinmvvm.core.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.viewnext.kotlinmvvm.FacturasApplication
import com.viewnext.kotlinmvvm.core.data.repository.RoomFacturasRepository
import com.viewnext.kotlinmvvm.data_retrofit.data.FacturasRepository
import com.viewnext.kotlinmvvm.domain.Factura
import com.viewnext.kotlinmvvm.domain.Filtros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed interface FacturasUiState {
    data class Succes(val facturas: List<Factura>) : FacturasUiState
    object Error : FacturasUiState
    object Loading : FacturasUiState
}

class FacturasViewModel(
    private val facturasRepository: FacturasRepository,
    private val localRepository: RoomFacturasRepository
) : ViewModel() {

    var facturasUiState: FacturasUiState by mutableStateOf(FacturasUiState.Loading)
        private set

    private var datosCargados = false

    private val filtroActual = MutableStateFlow(Filtros())
    private val _todasLasFacturas = MutableStateFlow<List<Factura>>(emptyList())

    init {
        cargarFacturas()
        observarRoom()
    }

    fun cargarFacturas() {
        viewModelScope.launch {
            facturasUiState = FacturasUiState.Loading
            try {
                if(!datosCargados) {
                    val response = facturasRepository.getFacturas()
                    localRepository.refreshFacturasFromNetwork(response.facturas)
                    datosCargados = true
                }
            } catch(e: IOException) {
                facturasUiState = FacturasUiState.Error
            } catch (e: HttpException) {
                facturasUiState = FacturasUiState.Error
            }
        }
    }

    fun observarRoom() {
        viewModelScope.launch {
            localRepository.getAllFacturasStream()
                .collect { facturas ->
                    _todasLasFacturas.value = facturas
                    aplicarFiltroSeleccionado(filtroActual.value, facturas)
                }
        }

        viewModelScope.launch {
            filtroActual.collect { filtro ->
                aplicarFiltroSeleccionado(filtro, _todasLasFacturas.value)
            }
        }
    }

    private fun aplicarFiltroSeleccionado(
        filtro: Filtros,
        listaFacturas: List<Factura>
    ) {
        val resultado = listaFacturas.filter { factura ->
            val fechaFactura = factura.fecha.toLongOrNull()
            val cumpleFecha =
                (filtro.fechaDesde == null || fechaFactura ?: Long.MAX_VALUE >= filtro.fechaDesde) &&
                (filtro.fechaHasta == null || fechaFactura ?: Long.MIN_VALUE <= filtro.fechaHasta)

            val cumpleImporte =
                (filtro.importeMin == null || factura.importe >= filtro.importeMin) &&
                (filtro.importeMax == null || factura.importe <= filtro.importeMax)

            val cumpleEstado = filtro.estados.isEmpty() || filtro.estados.contains(factura.estado)

            cumpleFecha && cumpleImporte && cumpleEstado
        }

        facturasUiState = FacturasUiState.Succes(resultado)
    }

    fun aplicarFiltros(nuevoFiltro: Filtros) {
        filtroActual.value = nuevoFiltro
    }

    fun eliminarFiltros() {
        filtroActual.value = Filtros()
    }

    fun recargarDesdeRed() {
        datosCargados = false
        cargarFacturas()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FacturasApplication)
                val container = application.container

                FacturasViewModel(
                    facturasRepository = container.facturasRepository,
                    localRepository = container.roomFacturasRepository
                )
            }
        }
    }
}