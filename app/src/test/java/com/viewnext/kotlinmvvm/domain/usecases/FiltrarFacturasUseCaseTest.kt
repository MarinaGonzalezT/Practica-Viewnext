package com.viewnext.kotlinmvvm.domain.usecases

import com.viewnext.kotlinmvvm.domain.model.Factura
import com.viewnext.kotlinmvvm.domain.model.Filtros
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.test.assertTrue

class FiltrarFacturasUseCaseTest {
    private val usecase = FiltrarFacturasUseCase()

    private val facturas = listOf(
        Factura(0, "Pendiente de pago", 56.38, "07/02/2019"),
        Factura(1, "Pagada", 51.2435, "05/02/2019"),
        Factura(2, "Pendiente de pago", 23.15, "08/01/2019"),
        Factura(3, "Anulada", 18.247777, "07/12/2018"),
        Factura(4, "Pagada", 47.1444, "16/11/2018")
    )

    @Test
    fun `filtra Facturas por estado correctamente`() {
        val filtros = Filtros(estados = listOf("Pagada"))

        val resultado = usecase(facturas, filtros)

        assertEquals(2, resultado.size)
        assertEquals("Pagada", resultado[0].estado)
        assertEquals("Pagada", resultado[1].estado)
    }

    @Test
    fun `filtra Facturas por importe correctamente`() {
        val filtro = Filtros(importeMin = 10f, importeMax = 30f)

        val resultado = usecase(facturas, filtro)

        assertEquals(2, resultado.size)
        assertEquals(23.15, resultado[0].importe, 0.1)
        assertEquals(18.25, resultado[1].importe, 0.1)
    }

    @Test
    fun `filtra Facturas por fecha correctamente`() {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val desde = formato.parse("01/02/2019")!!.time
        val hasta = formato.parse("01/03/2019")!!.time

        val filtro = Filtros(fechaDesde = desde, fechaHasta = hasta)

        val resultado = usecase(facturas, filtro)

        assertEquals(2, resultado.size)
        assertEquals("07/02/2019", resultado[0].fecha)
        assertEquals("05/02/2019", resultado[1].fecha)
    }

    @Test
    fun `al filtrar descarta las Facturas con fechas invalidas`() {
        val facturasInvalidas = listOf(
            Factura(0, "Pendiente de pago", 56.38, "07/02/2019"),
            Factura(1, "Pagada", 51.2435, ""),
            Factura(2, "Pagada", 12.0, "fechaInvalida")
        )

        val filtro = Filtros(fechaDesde = 0L, fechaHasta = System.currentTimeMillis())

        val resultado = usecase(facturasInvalidas, filtro)

        assertEquals(1, resultado.size)
    }

    @Test
    fun `si no hay filtro devuelve todas las Facturas`() {
        val filtro = Filtros()

        val resultado = usecase(facturas, filtro)

        assertEquals(5, resultado.size)
    }

    @Test
    fun `filtro con fechaDesde y sin fechaHasta filtra correctamente`() {
        val filtro = Filtros(fechaDesde = 1546300800000L) // 01/01/2019

        val resultado = usecase(facturas, filtro)

        assertEquals(3, resultado.size)
    }

    @Test
    fun `filtro con fechaHasta y sin fechaDesde filtra correctamente`() {
        val filtro = Filtros(fechaHasta = 1546300800000L) // 01/01/2019

        val resultado = usecase(facturas, filtro)

        assertEquals(2, resultado.size)
    }

    @Test
    fun `filtro con importeMin y sin importeMax filtra correctamente`() {
        val filtro = Filtros(importeMin = 30f)

        val resultado = usecase(facturas, filtro)

        assertEquals(3, resultado.size)
    }

    @Test
    fun `filtro con importeMax y sin importeMin filtra correctamente`() {
        val filtro = Filtros(importeMax = 30f)

        val resultado = usecase(facturas, filtro)

        assertEquals(2, resultado.size)
    }
}