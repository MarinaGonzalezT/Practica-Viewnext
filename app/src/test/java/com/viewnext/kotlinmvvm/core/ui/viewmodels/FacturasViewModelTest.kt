package com.viewnext.kotlinmvvm.core.ui.viewmodels

import com.viewnext.kotlinmvvm.core.data.repository.RoomFacturasRepository
import com.viewnext.kotlinmvvm.core.ui.FacturasUiState
import com.viewnext.kotlinmvvm.data_retrofit.repository.FacturasRepository
import com.viewnext.kotlinmvvm.domain.FacturasResponse
import com.viewnext.kotlinmvvm.domain.model.Factura
import com.viewnext.kotlinmvvm.domain.model.Filtros
import com.viewnext.kotlinmvvm.domain.usecases.FiltrarFacturasUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class FacturasViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var facturasRepository: FacturasRepository
    private lateinit var localRepository: RoomFacturasRepository
    private lateinit var viewModel: FacturasViewModel

    private val useCase = FiltrarFacturasUseCase()

    private val facturas = listOf(
        Factura(0, "Pendiente de pago", 56.38, "07/02/2019"),
        Factura(1, "Pagada", 51.2435, "05/02/2019"),
        Factura(2, "Pendiente de pago", 23.15, "08/01/2019"),
        Factura(3, "Anulada", 18.247777, "07/12/2018"),
        Factura(4, "Pagada", 47.1444, "16/11/2018")
    )

    @Before
    fun setUp() {
        runTest {
            Dispatchers.setMain(dispatcher)

            facturasRepository = mock(FacturasRepository::class.java)
            `when`(facturasRepository.getFacturas()).thenReturn(FacturasResponse(facturas.size, facturas))

            localRepository = mock(RoomFacturasRepository::class.java)
            `when`(localRepository.getAllFacturasStream()).thenReturn(flowOf(facturas))

            viewModel = FacturasViewModel(
                facturasRepository = facturasRepository,
                localRepository = localRepository,
                filtrarFacturasUseCase = useCase
            )
        }
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando carga facturas se emite Succes`() = runTest {
        viewModel.cargarFacturas()
        advanceUntilIdle()

        val estado = viewModel.facturasUiState.value
        assertTrue(estado is FacturasUiState.Succes)
        assertEquals(5, (estado as FacturasUiState.Succes).facturas.size)
    }

    @Test
    fun `cuando getFacturas lanza excepcion se emite Error`() = runTest {
        `when`(facturasRepository.getFacturas()).thenThrow(RuntimeException("Error de red"))

        FacturasViewModel.datosCargados = false
        viewModel.cargarFacturas()
        advanceUntilIdle()

        val estado = viewModel.facturasUiState.value
        assertTrue(estado is FacturasUiState.Error)
    }

    @Test
    fun `cuando se aplican filtros se muestran los resultados filtrados`() = runTest {
        val filtro = Filtros(estados = listOf("Pagada"))
        viewModel.aplicarFiltros(filtro)
        advanceUntilIdle()

        val estado = viewModel.facturasUiState.value
        assertTrue(estado is FacturasUiState.Succes)

        val facturasFiltradas = (estado as FacturasUiState.Succes).facturas
        assertEquals(2, facturasFiltradas.size)
        assertTrue(facturasFiltradas.all { it.estado == "Pagada" })
    }

    @Test
    fun `obtenerFiltrosActuales devuelve los filtros vigentes`() {
        val filtro = Filtros(importeMin = 20f, importeMax = 50f)
        viewModel.aplicarFiltros(filtro)

        val actual = viewModel.obtenerFiltrosActuales()
        assertEquals(filtro, actual)
    }

    @Test
    fun `si los datos ya estaban cargados no se vuelve a llamar a getFacturas`() = runTest {
        FacturasViewModel.datosCargados = true
        viewModel.cargarFacturas()
        advanceUntilIdle()

        verify(facturasRepository, never()).getFacturas()
    }

    @Test
    fun `cuando inicia la carga se emite Loading`() = runTest {
        viewModel.cargarFacturas()

        val estado = viewModel.facturasUiState.value
        assertEquals(FacturasUiState.Loading, estado)
    }

    @Test
    fun `refreshFacturasFromNetwork se llama si los datos no estaban cargados`() = runTest {
        viewModel.cargarFacturas()
        advanceUntilIdle()

        // Aunque se llama al cargarFacturas vemos que solo hace el refresh una vez.
        // Se llama al crear el viewmodel que se hace en la función setUp (con @Before)
        verify(localRepository, times(1)).refreshFacturasFromNetwork(any())
    }
}