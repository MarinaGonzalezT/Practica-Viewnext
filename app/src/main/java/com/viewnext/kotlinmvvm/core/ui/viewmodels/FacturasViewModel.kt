package com.viewnext.kotlinmvvm.core.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.viewnext.kotlinmvvm.MainApplication
import com.viewnext.kotlinmvvm.core.data.repository.RoomFacturasRepository
import com.viewnext.kotlinmvvm.core.ui.FacturasUiState
import com.viewnext.kotlinmvvm.data_retrofit.repository.FacturasRepository
import com.viewnext.kotlinmvvm.domain.model.Factura
import com.viewnext.kotlinmvvm.domain.model.Filtros
import com.viewnext.kotlinmvvm.domain.usecases.FiltrarFacturasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class FacturasViewModel(
    private val facturasRepository: FacturasRepository,
    private val localRepository: RoomFacturasRepository,
    private val filtrarFacturasUseCase: FiltrarFacturasUseCase
) : ViewModel() {

    private val _facturasUiState = MutableStateFlow<FacturasUiState>(FacturasUiState.Loading)
    val facturasUiState: StateFlow<FacturasUiState> = _facturasUiState

    private var filtrosActivos: Filtros = Filtros()

    init {
        Log.d("", "Creando viewmodel")
        cargarFacturas()
    }

    fun cargarFacturas() {
        viewModelScope.launch {
            _facturasUiState.value = FacturasUiState.Loading

            try {
                if(!datosCargados) {
                    Log.d("", "Cargando datos")
                    val response = facturasRepository.getFacturas()
                    localRepository.refreshFacturasFromNetwork(response.facturas)
                    datosCargados = true
                }
                localRepository.getAllFacturasStream().collect { facturas ->
                    val facturasFiltradas = aplicarFiltrosInterno(facturas)
                    _facturasUiState.value = FacturasUiState.Succes(facturasFiltradas)
                }
            } catch(e: IOException) {
                _facturasUiState.value = FacturasUiState.Error("Error de red: ${e.localizedMessage}")
            } catch (e: HttpException) {
                _facturasUiState.value = FacturasUiState.Error("Error del servidor: ${e.message}")
            }
        }
    }

    fun aplicarFiltros(filtros: Filtros) {
        filtrosActivos = filtros
        viewModelScope.launch {
            localRepository.getAllFacturasStream()
                .collect { facturas ->
                    val facturasFiltradas = aplicarFiltrosInterno(facturas)
                    _facturasUiState.value = FacturasUiState.Succes(facturasFiltradas)
                }
        }
    }

    fun obtenerFiltrosActuales(): Filtros = filtrosActivos

    private fun aplicarFiltrosInterno(facturas: List<Factura>): List<Factura> {
        return filtrarFacturasUseCase.invoke(facturas, filtrosActivos)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as MainApplication)
                val container = application.container

                FacturasViewModel(
                    facturasRepository = container.facturasRepository,
                    localRepository = container.roomFacturasRepository,
                    filtrarFacturasUseCase = container.filtrarFacturasUseCase
                )
            }
        }

        var datosCargados = false
    }
}