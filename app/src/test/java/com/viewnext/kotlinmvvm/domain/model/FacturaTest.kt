package com.viewnext.kotlinmvvm.domain.model

import junit.framework.TestCase.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test

class FacturaTest {
    @Test
    fun `crear Factura y acceder a propiedades`() {
        val factura = Factura(0, "Pagada", 19.24, "07/02/2019")

        assertEquals(0, factura.id)
        assertEquals("Pagada", factura.estado)
        assertEquals(19.24, factura.importe, 0.0)
        assertEquals("07/02/2019", factura.fecha)
    }

    @Test
    fun `se puede serializar y deserializar Factura`() {
        val factura = Factura(1, "Pagada", 99.99, "01/01/2024")

        val json = Json.encodeToString(factura)
        val resultado = Json.decodeFromString<Factura>(json)

        assertEquals(factura, resultado)
    }
}