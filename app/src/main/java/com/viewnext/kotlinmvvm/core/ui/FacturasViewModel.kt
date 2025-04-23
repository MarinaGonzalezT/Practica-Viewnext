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

    init {
        cargarFacturas()
        observarFacturasActuales()
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
                FacturasUiState.Error
            } catch (e: HttpException) {
                FacturasUiState.Error
            }
        }
    }

    fun observarFacturasActuales() {
        viewModelScope.launch {
            localRepository.getAllFacturasStream()
                .collect { facturas ->
                    facturasUiState = FacturasUiState.Succes(facturas)
                }
        }
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