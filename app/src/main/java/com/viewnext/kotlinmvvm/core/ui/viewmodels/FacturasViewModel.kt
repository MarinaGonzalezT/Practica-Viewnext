package com.viewnext.kotlinmvvm.core.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viewnext.kotlinmvvm.core.data.repository.RoomFacturasRepository
import com.viewnext.kotlinmvvm.core.ui.FacturasUiState
import com.viewnext.kotlinmvvm.data_retrofit.repository.FacturasRepository
import com.viewnext.kotlinmvvm.domain.model.Factura
import com.viewnext.kotlinmvvm.domain.model.Filtros
import com.viewnext.kotlinmvvm.domain.usecases.FiltrarFacturasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacturasViewModel @Inject constructor(
    private val facturasRepository: FacturasRepository,
    private val localRepository: RoomFacturasRepository,
    private val filtrarFacturasUseCase: FiltrarFacturasUseCase
) : ViewModel() {

    private val _facturasUiState = MutableStateFlow<FacturasUiState>(FacturasUiState.Loading)
    val facturasUiState: StateFlow<FacturasUiState> = _facturasUiState

    private var filtrosActivos: Filtros = Filtros()

    init {
        //Log.d("", "Creando viewmodel")
        cargarFacturas()
    }

    fun cargarFacturas() {
        _facturasUiState.value = FacturasUiState.Loading

        viewModelScope.launch {
            try {
                if(!datosCargados) {
                    //Log.d("", "Cargando datos")
                    val response = facturasRepository.getFacturas()
                    localRepository.refreshFacturasFromNetwork(response.facturas)
                    datosCargados = true
                }
                localRepository.getAllFacturasStream().collect { facturas ->
                    val facturasFiltradas = aplicarFiltrosInterno(facturas)
                    _facturasUiState.value = FacturasUiState.Succes(facturasFiltradas)
                }
            } catch(e: Exception) {
                _facturasUiState.value = FacturasUiState.Error("Error al cargar los datos")
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
        var datosCargados = false
    }
}