package com.viewnext.kotlinmvvm.domain.usecases

import com.viewnext.kotlinmvvm.domain.model.Factura
import com.viewnext.kotlinmvvm.domain.model.Filtros
import java.text.SimpleDateFormat
import java.util.Locale

class FiltrarFacturasUseCase {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    operator fun invoke(
        facturas: List<Factura>,
        filtro: Filtros
    ): List<Factura> {
        return facturas.filter { factura ->
            val fechaFactura = try {
                dateFormat.parse(factura.fecha)?.time
            } catch (e: Exception) {
                null
            }

            val cumpleFecha = (fechaFactura != null) &&
                (filtro.fechaDesde == null || fechaFactura >= filtro.fechaDesde) &&
                (filtro.fechaHasta == null || fechaFactura <= filtro.fechaHasta)

            val cumpleImporte =
                (factura.importe >= filtro.importeMin) &&
                (factura.importe <= filtro.importeMax)

            val cumpleEstado = filtro.estados.isEmpty() || filtro.estados.contains(factura.estado)

            cumpleFecha && cumpleImporte && cumpleEstado
        }
    }
}