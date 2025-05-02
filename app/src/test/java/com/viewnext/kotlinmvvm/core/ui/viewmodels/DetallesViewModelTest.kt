package com.viewnext.kotlinmvvm.core.ui.viewmodels

import com.viewnext.kotlinmvvm.data_retrofit.repository.DetallesRepository
import com.viewnext.kotlinmvvm.domain.model.Detalles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class DetallesViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: DetallesRepository
    private lateinit var viewModel: DetallesViewModel

    private val detalles = Detalles(
        cau = "ES00210000000001994LJ1FA000",
        estado = "No hemos recibido ninguna solicitud de autoconsumo",
        tipo = "Con excedentes y compensación individual - Consumo",
        compensacion = "Precio PVPC",
        potencia = "5kWp"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        repository = mock(DetallesRepository::class.java)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando getDetalles va bien se actualiza el estado correctamente`() = runTest {
        `when`(repository.getDetalles()).thenReturn(detalles)

        viewModel = DetallesViewModel(repository)
        advanceUntilIdle()

        assertEquals(detalles, viewModel.detalles)
        assertFalse(viewModel.error)
    }

    @Test
    fun `cuando getDetalles lanza excepcion se activa error`() = runTest {
        `when`(repository.getDetalles()).thenThrow(RuntimeException())

        viewModel = DetallesViewModel(repository)
        advanceUntilIdle()

        assertNull(viewModel.detalles)
        assertTrue(viewModel.error)
    }
}