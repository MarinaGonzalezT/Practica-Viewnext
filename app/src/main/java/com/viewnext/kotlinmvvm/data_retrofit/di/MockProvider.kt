package com.viewnext.kotlinmvvm.data_retrofit.di

import com.viewnext.kotlinmvvm.data_retrofit.repository.FacturasRepository

object MockProvider {
    private var mockActivado: Boolean = false
    private var _repositorio: FacturasRepository? = null

    fun alternarMock() {
        mockActivado = !mockActivado
        _repositorio = null
    }

    fun isMocking(): Boolean = mockActivado
}