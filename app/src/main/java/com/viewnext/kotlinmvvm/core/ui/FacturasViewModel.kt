package com.viewnext.kotlinmvvm.core.ui

import android.util.Log
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
import com.viewnext.kotlinmvvm.data_retrofit.FacturasRepository
import com.viewnext.kotlinmvvm.domain.model.Factura
import com.viewnext.kotlinmvvm.domain.model.Filtros
import com.viewnext.kotlinmvvm.domain.usecases.FiltrarFacturasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
    private val localRepository: RoomFacturasRepository,
    private val filtrarFacturasUseCase: FiltrarFacturasUseCase
) : ViewModel() {

    var facturasUiState: FacturasUiState by mutableStateOf(FacturasUiState.Loading)
        private set


    private val todasLasFacturas = MutableStateFlow<List<Factura>>(emptyList())
    private val filtroActual = MutableStateFlow(Filtros())

    init {
        cargarFacturas()
        observarDatosLocales()
    }

    fun cargarFacturas() {
        viewModelScope.launch {
            facturasUiState = FacturasUiState.Loading
            try {
                if(!datosCargados) {
                    Log.d("", "Cargando datos")
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

    fun observarDatosLocales() {
        viewModelScope.launch {
            combine(todasLasFacturas, filtroActual) { facturas, filtro ->
                filtrarFacturasUseCase(facturas, filtro)
            }.collect { facturasFiltradas ->
                facturasUiState = FacturasUiState.Succes(facturasFiltradas)
            }
        }

        viewModelScope.launch {
            localRepository.getAllFacturasStream().collect {
                todasLasFacturas.value = it
            }
        }
    }

    fun aplicarFiltros(filtros: Filtros) {
        filtroActual.value = filtros
    }

    fun obtenerFiltrosActuales(): Filtros {
        return filtroActual.value
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FacturasApplication)
                val container = application.container

                FacturasViewModel(
                    facturasRepository = container.facturasRepository,
                    localRepository = container.roomFacturasRepository,
                    filtrarFacturasUseCase = container.filtrarFacturasUseCase
                )
            }
        }

        private var datosCargados = false

        fun resetearDatos(){
            datosCargados = !datosCargados
        }
    }
}