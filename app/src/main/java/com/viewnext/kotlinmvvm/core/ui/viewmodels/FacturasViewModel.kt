package com.viewnext.kotlinmvvm.core.ui.viewmodels

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

class FacturasViewModel(private val facturasRepository: FacturasRepository) : ViewModel() {
    var facturasUiState: FacturasUiState by mutableStateOf(FacturasUiState.Loading)
        private set

    init {
        cargarFacturas()
    }

    fun cargarFacturas() {
        viewModelScope.launch {
            facturasUiState = FacturasUiState.Loading
            facturasUiState = try {
                FacturasUiState.Succes(facturasRepository.getFacturas().facturas)
            } catch(e: IOException) {
                FacturasUiState.Error
            } catch (e: HttpException) {
                FacturasUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FacturasApplication)
                val facturasRepository = application.container.facturasRepository
                FacturasViewModel(facturasRepository = facturasRepository)
            }
        }
    }
}