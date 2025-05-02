package com.viewnext.kotlinmvvm.data_retrofit.repository

import com.viewnext.kotlinmvvm.data_retrofit.apiService.FacturasApiService
import com.viewnext.kotlinmvvm.domain.FacturasResponse
import com.viewnext.kotlinmvvm.domain.model.Factura
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class NetworkFacturasRepositoryTest {
    private lateinit var apiService: FacturasApiService
    private lateinit var repository: FacturasRepository

    private val mockResponse = FacturasResponse(
        numFacturas = 5,
        facturas = listOf(
            Factura(0, "Pendiente de pago", 56.38, "07/02/2019"),
            Factura(1, "Pagada", 51.2435, "05/02/2019"),
            Factura(2, "Pendiente de pago", 23.15, "08/01/2019"),
            Factura(3, "Anulada", 18.247777, "07/12/2018"),
            Factura(4, "Pagada", 47.1444, "16/11/2018")
        )
    )

    @Before
    fun setUp() {
        apiService = mock(FacturasApiService::class.java)
        repository = NetworkFacturasRepository(apiService)
    }

    @Test
    fun `getFacturas devuelve la lista correcta desde API`() = runTest {
        `when`(apiService.getFacturas()).thenReturn(mockResponse)

        val resultado = repository.getFacturas()

        assertEquals(5, resultado.numFacturas)
        assertEquals(5, resultado.facturas.size)
        verify(apiService).getFacturas()
    }
}