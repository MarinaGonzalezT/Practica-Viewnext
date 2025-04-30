package com.viewnext.kotlinmvvm.core.ui.viewmodels

import kotlinx.coroutines.test.StandardTestDispatcher

class FacturasViewModelTest {
    private val testDispatcher = StandardTestDispatcher()


    private val viewModel = FacturasViewModel(
        facturasRepository = TODO(),
        localRepository = TODO(),
        filtrarFacturasUseCase = TODO()
    )
}