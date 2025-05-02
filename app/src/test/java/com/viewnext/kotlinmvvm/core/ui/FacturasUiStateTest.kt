package com.viewnext.kotlinmvvm.core.ui

import com.viewnext.kotlinmvvm.domain.model.Factura
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

class FacturasUiStateTest {
    private val facturas = listOf(
        Factura(0, "Pendiente de pago", 56.38, "07/02/2019"),
        Factura(1, "Pagada", 51.2435, "05/02/2019"),
        Factura(2, "Pendiente de pago", 23.15, "08/01/2019"),
        Factura(3, "Anulada", 18.247777, "07/12/2018"),
        Factura(4, "Pagada", 47.1444, "16/11/2018")
    )

    @Test
    fun `Succes contiene lista de facturas`() {
        val estado = FacturasUiState.Succes(facturas)

        assertTrue(estado is FacturasUiState.Succes)
        assertEquals(5, estado.facturas.size)
        Assert.assertEquals("Pendiente de pago", facturas[0].estado)
        Assert.assertEquals("Pagada", facturas[1].estado)
        Assert.assertEquals("Pendiente de pago", facturas[2].estado)
        Assert.assertEquals("Anulada", facturas[3].estado)
        Assert.assertEquals("Pagada", facturas[4].estado)
    }

    @Test
    fun `Error contiene mensaje correcto`() {
        val mensaje = "Algo salió mal"
        val estado = FacturasUiState.Error(mensaje)

        assertTrue(estado is FacturasUiState.Error)
        assertEquals(mensaje, estado.mensaje)
    }

    @Test
    fun `Loading es singleton y puede ser usado`() {
        val estado: FacturasUiState = FacturasUiState.Loading

        assertTrue(estado is FacturasUiState.Loading)
        assertEquals(FacturasUiState.Loading, estado)
    }
}