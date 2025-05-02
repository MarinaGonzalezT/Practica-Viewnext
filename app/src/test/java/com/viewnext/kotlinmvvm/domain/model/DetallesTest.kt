package com.viewnext.kotlinmvvm.domain.model

import junit.framework.TestCase.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test

class DetallesTest {
    val detalles = Detalles(
        cau = "ES12345",
        estado = "Sin solicitud",
        tipo = "Con excedentes",
        compensacion = "Si",
        potencia = "5kWp"
    )

    @Test
    fun `acceder a las propiedades propiedades`() {
        assertEquals("ES12345", detalles.cau)
        assertEquals("Sin solicitud", detalles.estado)
        assertEquals("Con excedentes", detalles.tipo)
        assertEquals("Si", detalles.compensacion)
        assertEquals("5kWp", detalles.potencia)
    }

    @Test
    fun `se puede serializar y deserializar Detalles`() {
        val json = Json.encodeToString(detalles)
        val resultado = Json.decodeFromString<Detalles>(json)

        assertEquals(detalles, resultado)
    }
}