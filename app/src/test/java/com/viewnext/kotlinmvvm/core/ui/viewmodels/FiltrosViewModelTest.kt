package com.viewnext.kotlinmvvm.core.ui.viewmodels

import com.viewnext.kotlinmvvm.domain.model.Filtros
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class FiltrosViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var facturasViewModel: FacturasViewModel
    private lateinit var filtrosViewModel: FiltrosViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        val filtrosIniciales = Filtros(importeMin = 10f, importeMax = 100f)

        facturasViewModel = mock(FacturasViewModel::class.java)
        `when`(facturasViewModel.obtenerFiltrosActuales()).thenReturn(filtrosIniciales)

        filtrosViewModel = FiltrosViewModel(facturasViewModel)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al actualizar fechaDesde se modifica solo ese campo`() = runTest {
        val nuevaFecha = 123456789L
        val anterior = filtrosViewModel.filtros.value

        filtrosViewModel.actualizarFechaDesde(nuevaFecha)
        assertEquals(nuevaFecha, filtrosViewModel.filtros.value.fechaDesde)
        assertEquals(anterior.fechaHasta, filtrosViewModel.filtros.value.fechaHasta)
    }

    @Test
    fun `actualizarFechaDesde no permite una fecha posterior a fechaHasta`() = runTest {
        val fechaActual = System.currentTimeMillis()
        val desde = fechaActual
        val hasta = fechaActual - 100000L

        filtrosViewModel.actualizarFechaHasta(hasta)

        val job = launch {
            filtrosViewModel.mensajeError.collect { mensaje ->
                assertEquals("La fecha 'Desde' debe ser anterior a la fecha 'Hasta'", mensaje)
            }
            filtrosViewModel.actualizarFechaDesde(desde)
            assertNull(filtrosViewModel.filtros.value.fechaDesde)
            assertNull(filtrosViewModel.filtros.value.fechaHasta)
        }

        job.cancel()
    }

    @Test
    fun `actualizarFechaDesde con null borra la fechaDesde`() = runTest {
        filtrosViewModel.actualizarFechaDesde(null)
        assertNull(filtrosViewModel.filtros.value.fechaDesde)
    }

    @Test
    fun `actualizarFechaHasta con null borra la fechaHasta`() = runTest {
        filtrosViewModel.actualizarFechaHasta(null)
        assertNull(filtrosViewModel.filtros.value.fechaHasta)
    }

    @Test
    fun `companion provideFactory crea correctamente el viewModel`() {
        val factory = FiltrosViewModel.provideFactory(facturasViewModel)

        val viewModel = factory.create(FiltrosViewModel::class.java)

        assertNotNull(viewModel)
    }
}