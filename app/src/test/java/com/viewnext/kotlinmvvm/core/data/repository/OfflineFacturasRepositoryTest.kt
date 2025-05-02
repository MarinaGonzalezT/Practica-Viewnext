package com.viewnext.kotlinmvvm.core.data.repository

import com.viewnext.kotlinmvvm.core.data.database.FacturaDao
import com.viewnext.kotlinmvvm.domain.model.Factura
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class OfflineFacturasRepositoryTest {
    private lateinit var facturaDao: FacturaDao
    private lateinit var repository: OfflineFacturasRepository

    private val mockFacturas = listOf(
        Factura(0, "Pendiente de pago", 56.38, "07/02/2019"),
        Factura(1, "Pagada", 51.2435, "05/02/2019"),
        Factura(2, "Pendiente de pago", 23.15, "08/01/2019"),
        Factura(3, "Anulada", 18.247777, "07/12/2018"),
        Factura(4, "Pagada", 47.1444, "16/11/2018")
    )

    @Before
    fun setUp() {
        facturaDao = mock(FacturaDao::class.java)
        repository = OfflineFacturasRepository(facturaDao)

        `when`(facturaDao.getAllFacturas()).thenReturn(flowOf(mockFacturas))
    }

    @Test
    fun `getAllFacturasStream devuelve el flujo correcto`() = runTest {
        val resultado = repository.getAllFacturasStream()

        resultado.collect { facturas ->
            assertEquals(5, facturas.size)
            assertEquals("Pendiente de pago", facturas[0].estado)
            assertEquals("Pagada", facturas[1].estado)
            assertEquals("Pendiente de pago", facturas[2].estado)
            assertEquals("Anulada", facturas[3].estado)
            assertEquals("Pagada", facturas[4].estado)
        }

        verify(facturaDao).getAllFacturas()
    }

    @Test
    fun `refreshFacturasFromNetwork elimina e inserta nuevas Facturas`() = runTest {
        val nuevasFacturas = listOf(
            Factura(0, "Pagada", 13.52, "14/02/2019"),
            Factura(1, "Pendiente de pago", 24.15, "03/01/2019")
        )

        repository.refreshFacturasFromNetwork(nuevasFacturas)

        verify(facturaDao).deleteAll()
        verify(facturaDao).insertAll(nuevasFacturas)
    }
}