package com.viewnext.kotlinmvvm.domain.usecases

import com.viewnext.kotlinmvvm.domain.model.Factura
import com.viewnext.kotlinmvvm.domain.model.Filtros

class FiltrarFacturasUseCase {
    operator fun invoke(
        facturas: List<Factura>,
        filtro: Filtros
    ): List<Factura> {
        return facturas.filter { factura ->
            val fechaFactura = factura.fecha.toLongOrNull()
            val cumpleFecha =
                (filtro.fechaDesde == null || (fechaFactura ?: Long.MAX_VALUE) >= filtro.fechaDesde) &&
                (filtro.fechaHasta == null || (fechaFactura ?: Long.MIN_VALUE) <= filtro.fechaHasta)

            val cumpleImporte =
                (filtro.importeMin == null || factura.importe >= filtro.importeMin) &&
                (filtro.importeMax == null || factura.importe <= filtro.importeMax)

            val cumpleEstado = filtro.estados.isEmpty() || filtro.estados.contains(factura.estado)

            cumpleFecha && cumpleImporte && cumpleEstado
        }
    }
}