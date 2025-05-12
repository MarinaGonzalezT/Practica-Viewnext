package com.viewnext.kotlinmvvm.core.ui.screens.facturas

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.core.ui.screens.CuadroFechas
import com.viewnext.kotlinmvvm.core.ui.screens.FechaPicker
import com.viewnext.kotlinmvvm.core.ui.screens.Titulo
import com.viewnext.kotlinmvvm.core.ui.viewmodels.FacturasViewModel
import com.viewnext.kotlinmvvm.core.ui.viewmodels.FiltrosViewModel
import com.viewnext.kotlinmvvm.domain.model.Filtros
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun PantallaFiltros(
    navController: NavController
) {
    val facturasEntry = remember { navController.getBackStackEntry("Facturas") }
    val facturasViewModel = viewModel<FacturasViewModel>(facturasEntry)
    val filtrosViewModel = viewModel<FiltrosViewModel>(
        factory = FiltrosViewModel.provideFactory(facturasViewModel)
    )
    val filtros by filtrosViewModel.filtros.collectAsState()
    val scrollState = rememberScrollState()

    val maxImporte = facturasViewModel.maxImporte

    val importeInicial = if(filtros.importeMin == 0f && filtros.importeMax == 300f) {
        0f..maxImporte
    } else {
        filtros.importeMin..filtros.importeMax
    }

    var importe by remember { mutableStateOf(importeInicial) }

    var fechaDesde = filtros.fechaDesde
    var fechaHasta = filtros.fechaHasta

    val estados = listOf("Pagada", "Anulada", "Cuota Fija", "Pendiente de pago", "Plan de pago")
    val estadosSeleccionados = remember {
        mutableStateMapOf<String, Boolean>().apply {
            estados.forEach { estado ->
                put(estado, filtros.estados.contains(estado))
            }
        }
    }

    Scaffold(
        topBar = {
            FiltrosTopBar(
                onClose = { navController.popBackStack("Facturas", inclusive = false) },
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Titulo(stringResource(R.string.Filtros_filtra_facturas))

            SeccionFechas(
                fechaDesde = fechaDesde,
                fechaHasta = fechaHasta,
                onFechaDesdeChange = { filtrosViewModel.actualizarFechaDesde(it) },
                onFechaHastaChange = { filtrosViewModel.actualizarFechaHasta(it) }
            )

            SeccionImporte(
                importe = importe,
                onImporteChange = { importe = it },
                maxImporte = maxImporte
            )

            SeccionChecks(estadosSeleccionados)

            SeccionBotones(
                onApply = {
                    filtrosViewModel.actualizarFiltros(
                        Filtros(
                            fechaDesde = fechaDesde,
                            fechaHasta = fechaHasta,
                            importeMin = importe.start,
                            importeMax = importe.endInclusive,
                            estados = estadosSeleccionados.filterValues { it }.keys.toList()
                        )
                    )
                    navController.popBackStack("Facturas", inclusive = false)
                },
                onDelete = {
                    filtrosViewModel.eliminarFiltros()
                    navController.popBackStack("Facturas", inclusive = false)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosTopBar(
    onClose: () -> Unit
) {
    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = onClose) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = colorResource(R.color.gris),
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
        }
    )
}

@Composable
fun SeccionFechas(
    fechaDesde: Long?,
    fechaHasta: Long?,
    onFechaDesdeChange: (Long?) -> Unit,
    onFechaHastaChange: (Long?) -> Unit
) {
    var mostrandoPickerPara by remember { mutableStateOf<String?>(null) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaHoy = remember { System.currentTimeMillis() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 20.dp)
    ) {
        Text(
            stringResource(R.string.Filtros_por_fecha),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = 12.dp, bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 40.dp)
        ) {
            CuadroFechas(
                etiqueta = stringResource(R.string.Filtros_et_desde),
                fecha = fechaDesde,
                onClick = { mostrandoPickerPara = "desde" },
                dateFormatter = dateFormatter,
                modifier = Modifier.weight(1f)
            )

            CuadroFechas(
                etiqueta = stringResource(R.string.Filtros_et_hasta),
                fecha = fechaHasta,
                onClick = { mostrandoPickerPara = "hasta" },
                dateFormatter = dateFormatter,
                modifier = Modifier.weight(1f)
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

    if (mostrandoPickerPara != null) {
        FechaPicker(
            fechaSeleccionada = when(mostrandoPickerPara) {
                "desde" -> fechaDesde
                "hasta" -> fechaHasta
                else -> null
            },
            onDateSelected = { millis ->
                when (mostrandoPickerPara) {
                    "desde" -> onFechaDesdeChange(millis)
                    "hasta" -> onFechaHastaChange(millis)
                }
                mostrandoPickerPara = null
            },
            onDismiss = { mostrandoPickerPara = null },
            minFecha = when(mostrandoPickerPara) {
                "hasta" -> fechaDesde
                else -> null
            },
            maxFecha = when(mostrandoPickerPara) {
                "desde" -> fechaHasta ?: fechaHoy
                else -> fechaHoy
            },
            tipo = mostrandoPickerPara ?: ""
        )
    }
}

@Composable
fun SeccionImporte(
    importe: ClosedFloatingPointRange<Float>,
    onImporteChange: (ClosedFloatingPointRange<Float>) -> Unit,
    maxImporte: Float
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 20.dp)
    ) {
        Text(
            stringResource(R.string.Filtros_por_importe),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0 €",
                color = colorResource(R.color.gris),
                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
            )
            Text(
                text = "${importe.start.toInt()} € - ${importe.endInclusive.toInt()} €",
                color = colorResource(R.color.verde),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${maxImporte.toInt()} €",
                color = colorResource(R.color.gris),
                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
            )
        }

        RangeSlider(
            value = importe,
            onValueChange = onImporteChange,
            valueRange = 0f..maxImporte,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(R.color.verde),
                activeTrackColor = colorResource(R.color.verde),
                inactiveTrackColor = colorResource(R.color.gris_claro)
            ),
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 20.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
fun SeccionChecks(
    estadosSeleccionados: MutableMap<String, Boolean>
) {
    val estados = mapOf(
        stringResource(R.string.Facturas_Estados_pagadas) to "Pagada",
        stringResource(R.string.Facturas_Estados_anuladas) to "Anulada",
        stringResource(R.string.Facturas_Estados_cuota_fija) to "Cuota Fija",
        stringResource(R.string.Facturas_Estados_pendientes_pago) to "Pendiente de pago",
        stringResource(R.string.Facturas_Estados_plan_pago) to "Plan de pago",
    )

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 20.dp)
    ) {
        Text(
            stringResource(R.string.Filtros_por_estado),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = 12.dp, bottom = 12.dp)
        )

        estados.forEach { (visible, real) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .size(height = 36.dp, width = 200.dp)
                    .clickable {
                        estadosSeleccionados[real] = !(estadosSeleccionados[real] ?: false)
                    }
            ) {
                Checkbox(
                    checked = estadosSeleccionados[real] == true,
                    onCheckedChange = { estadosSeleccionados[real] = it },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = colorResource(R.color.gris)
                    )
                )
                Text(text = visible)
            }
        }
    }
}

@Composable
fun SeccionBotones(
    onApply: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Button(
            onClick = onApply,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.verde)
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.Filtros_aplicar))
        }

        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.gris)
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.Filtros_elimina_filtros))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFiltros() {
    PantallaFiltros(
        navController = rememberNavController()
    )
}