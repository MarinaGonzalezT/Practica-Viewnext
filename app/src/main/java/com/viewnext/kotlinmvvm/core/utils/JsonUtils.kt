package com.viewnext.kotlinmvvm.core.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.viewnext.kotlinmvvm.domain.Factura
import com.viewnext.kotlinmvvm.domain.FacturasResponse

object JsonUtils {
    fun cargarFacturasDesdeJson(context: Context): List<Factura> {
        return try {
            val json = context.assets.open("facturas.json").bufferedReader().use {
                it.readText()
            }
            println("JSON leido: $json")

            val tipo = object : TypeToken<FacturasResponse>() {}.type
            val response: FacturasResponse = Gson().fromJson(json, tipo)

            println("Facturas parseadas: ${response.facturas.size}")
            response.facturas
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }

    }
}