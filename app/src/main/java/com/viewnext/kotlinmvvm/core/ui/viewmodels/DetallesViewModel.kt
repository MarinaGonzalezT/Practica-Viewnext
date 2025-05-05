package com.viewnext.kotlinmvvm.core.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viewnext.kotlinmvvm.data_retrofit.repository.DetallesRepository
import com.viewnext.kotlinmvvm.domain.model.Detalles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallesViewModel @Inject constructor(
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
}