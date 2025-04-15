package com.viewnext.kotlinmvvm.core.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viewnext.kotlinmvvm.core.utils.JsonUtils
import com.viewnext.kotlinmvvm.domain.Factura
import java.text.SimpleDateFormat
import java.util.Locale

class FacturasViewModel(application: Application) : AndroidViewModel(application) {
    private val _facturas = MutableLiveData<List<Factura>>()
    val facturas: LiveData<List<Factura>> = _facturas

    init {
        cargarFacturas()
    }

    private fun cargarFacturas() {
        val context = getApplication<Application>()
        val originales = JsonUtils.cargarFacturasDesdeJson(context)

        _facturas.value = originales.map {
            it.copy(fecha = formatearFecha(it.fecha))
        }
    }

    private fun formatearFecha(fechaOriginal: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
            val fecha = inputFormat.parse(fechaOriginal)
            val fechaFormateada = outputFormat.format(fecha!!)

            val partes = fechaFormateada.split(" ")
            val dia = partes[0]
            val mes = partes[1].replaceFirstChar { it.uppercaseChar() }
            val anio = partes[2]

            "$dia $mes $anio"
        } catch (e: Exception) {
            fechaOriginal
        }
    }
}