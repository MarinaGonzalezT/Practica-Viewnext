package com.viewnext.kotlinmvvm.data_retrofit.repository

import com.viewnext.kotlinmvvm.data_retrofit.apiService.SmartSolarApiService
import com.viewnext.kotlinmvvm.domain.model.Detalles
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class NetworkDetallesRepositoryTest {
    private lateinit var apiService: SmartSolarApiService
    private lateinit var repository: NetworkDetallesRepository

    private val detalles = Detalles(
        cau = "ES00210000000001994LJ1FA000",
        estado = "No hemos recibido ninguna solicitud de autoconsumo",
        tipo = "Con excedentes y compensación individual - Consumo",
        compensacion = "Precio PVPC",
        potencia = "5kWp"
    )

    @Before
    fun setUp() {
        apiService = mock(SmartSolarApiService::class.java)
        repository = NetworkDetallesRepository(apiService)
    }

    @Test
    fun `getDetalles devuelve el resultado correcto desde API`() = runTest {
        `when`(apiService.getDetalles()).thenReturn(detalles)

        val resultado = repository.getDetalles()

        assertEquals(detalles, resultado)
        verify(apiService).getDetalles()
    }
}