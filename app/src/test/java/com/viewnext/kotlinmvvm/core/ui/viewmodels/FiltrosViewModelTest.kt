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
    fun `al actualizar filtros se actualiza el StateFlow y se llama a aplicarFiltros`() = runTest {
        val nuevos = Filtros(importeMin = 20f, importeMax = 50f)
        filtrosViewModel.actualizarFiltros(nuevos)

        assertEquals(nuevos, filtrosViewModel.filtros.value)
        verify(facturasViewModel).aplicarFiltros(nuevos)
    }

    @Test
    fun `al eliminar filtros se reinicia el StateFlow y se llama a aplicarFiltros sin Filtros`() = runTest {
        filtrosViewModel.eliminarFiltros()

        assertEquals(Filtros(), filtrosViewModel.filtros.value)
        verify(facturasViewModel).aplicarFiltros(Filtros())
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
    fun `actualizarFechaDesde no permite una fecha posterior a la actual`() = runTest {
        val fechaActual = System.currentTimeMillis()
        val invalida = fechaActual + 1000000L

        val job = launch {
            filtrosViewModel.mensajeError.collect { mensaje ->
                assertEquals("No puedes seleccionar una fecha posterior a hoy", mensaje)
            }
            filtrosViewModel.actualizarFechaDesde(invalida)
            assertNull(filtrosViewModel.filtros.value.fechaDesde)
            assertNull(filtrosViewModel.filtros.value.fechaHasta)
        }

        job.cancel()
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
    fun `al actualizar fechaHasta se modifica solo ese campo`() = runTest {
        val nuevaFecha = 987654321L
        val anterior = filtrosViewModel.filtros.value

        filtrosViewModel.actualizarFechaHasta(nuevaFecha)
        assertEquals(nuevaFecha, filtrosViewModel.filtros.value.fechaHasta)
        assertEquals(anterior.fechaDesde, filtrosViewModel.filtros.value.fechaDesde)
    }

    @Test
    fun `actualizarFechaHasta no permite una fecha posterior a la actual`() = runTest {
        val fechaActual = System.currentTimeMillis()
        val invalida = fechaActual + 1000000L

        val job = launch {
            filtrosViewModel.mensajeError.collect { mensaje ->
                assertEquals("No puedes seleccionar una fecha posterior a hoy", mensaje)
            }
            filtrosViewModel.actualizarFechaHasta(invalida)
            assertNull(filtrosViewModel.filtros.value.fechaDesde)
            assertNull(filtrosViewModel.filtros.value.fechaHasta)
        }

        job.cancel()
    }

    @Test
    fun `actualizarFechaHasta no permite una fecha anterior a fechaDesde`() = runTest {
        val fechaActual = System.currentTimeMillis()
        val desde = fechaActual
        val hasta = fechaActual - 100000L

        filtrosViewModel.actualizarFechaDesde(desde)

        val job = launch {
            filtrosViewModel.mensajeError.collect { mensaje ->
                assertEquals("La fecha 'Hasta' debe ser posterior a la fecha 'Desde'", mensaje)
            }
            filtrosViewModel.actualizarFechaHasta(hasta)
            assertNull(filtrosViewModel.filtros.value.fechaDesde)
            assertNull(filtrosViewModel.filtros.value.fechaHasta)
        }

        job.cancel()
    }

    @Test
    fun `companion provideFactory crea correctamente el viewModel`() {
        val factory = FiltrosViewModel.provideFactory(facturasViewModel)

        val viewModel = factory.create(FiltrosViewModel::class.java)

        assertNotNull(viewModel)
    }
}