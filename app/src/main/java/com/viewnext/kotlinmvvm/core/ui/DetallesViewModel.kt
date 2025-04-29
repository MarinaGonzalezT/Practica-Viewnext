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
import com.viewnext.kotlinmvvm.MainApplication
import com.viewnext.kotlinmvvm.data_retrofit.repository.DetallesRepository
import com.viewnext.kotlinmvvm.domain.model.Detalles
import kotlinx.coroutines.launch

class DetallesViewModel(
    private val repository: DetallesRepository
) : ViewModel() {
    var detalles by mutableStateOf<Detalles?>(null)
        private set

    var error by mutableStateOf(false)
        private set

    init {
        cargarDetalles()
    }

    fun cargarDetalles() {
        viewModelScope.launch {
            try {
                detalles = repository.getDetalles()
            } catch (e: Exception) {
                error = true
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val container = application.container

                DetallesViewModel(container.detallesRepository)
            }
        }
    }
}