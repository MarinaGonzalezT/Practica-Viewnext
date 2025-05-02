package com.viewnext.kotlinmvvm.domain

import com.viewnext.kotlinmvvm.domain.model.Factura
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test

class FacturasResponseTest {
    val response = FacturasResponse(
        numFacturas = 5,
        facturas = listOf(
            Factura(0, "Pendiente de pago", 56.38, "07/02/2019"),
            Factura(1, "Pagada", 51.2435, "05/02/2019"),
            Factura(2, "Pendiente de pago", 23.15, "08/01/2019"),
            Factura(3, "Anulada", 18.247777, "07/12/2018"),
            Factura(4, "Pagada", 47.1444, "16/11/2018")
        )
    )

    @Test
    fun `se puede serializar y deserializar FacturasResponse`() {
        val json = Json.encodeToString(response)
        val resultado = Json.decodeFromString<FacturasResponse>(json)

        assertEquals(response, resultado)
    }
}