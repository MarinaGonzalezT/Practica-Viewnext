package com.viewnext.kotlinmvvm.core.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.material3.TopAppBarDefaults
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
import com.viewnext.kotlinmvvm.core.ui.FacturasViewModel
import com.viewnext.kotlinmvvm.core.ui.FiltrosViewModel
import com.viewnext.kotlinmvvm.domain.model.Filtros
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun PantallaFiltros(
    navController: NavController
) {
    val facturasEntry = remember { navController.getBackStackEntry("Facturas") }
    val facturasViewModel = viewModel<FacturasViewModel>(
            facturasEntry,
            factory = FacturasViewModel.Factory
    )
    val filtrosViewModel = viewModel<FiltrosViewModel>(
        factory = FiltrosViewModel.provideFactory(facturasViewModel)
    )
    val filtros by filtrosViewModel.filtros.collectAsState()

    val minImporte = filtros.importeMin
    val maxImporte = filtros.importeMax
    var importe by remember { mutableStateOf(minImporte..maxImporte) }

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
        },
        containerColor = colorResource(R.color.white)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Titulo(stringResource(R.string.filtra_facturas))

            SeccionFechas(
                fechaDesde = fechaDesde,
                fechaHasta = fechaHasta,
                onFechaDesdeChange = { filtrosViewModel.actualizarFechaDesde(it) },
                onFechaHastaChange = { filtrosViewModel.actualizarFechaHasta(it) }
            )

            SeccionImporte(
                importe = importe,
                onImporteChange = { importe = it },
                minImporte = minImporte,
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
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.white)
        )
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 20.dp)
    ) {
        Text(
            stringResource(R.string.por_fecha),
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
                etiqueta = "Desde:",
                fecha = fechaDesde,
                onClick = { mostrandoPickerPara = "desde" },
                dateFormatter = dateFormatter,
                modifier = Modifier.weight(1f)
            )

            CuadroFechas(
                etiqueta = "Hasta:",
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
            onDateSelected = { millis ->
                when (mostrandoPickerPara) {
                    "desde" -> onFechaDesdeChange(millis)
                    "hasta" -> onFechaHastaChange(millis)
                }
                mostrandoPickerPara = null
            },
            onDismiss = { mostrandoPickerPara = null }
        )
    }
}

@Composable
fun SeccionImporte(
    importe: ClosedFloatingPointRange<Float>,
    onImporteChange: (ClosedFloatingPointRange<Float>) -> Unit,
    minImporte: Float,
    maxImporte: Float
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 20.dp)
    ) {
        Text(
            stringResource(R.string.por_importe),
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
                text = "${minImporte.toInt()} €",
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
            valueRange = 1f..300f,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(R.color.verde),
                activeTrackColor = colorResource(R.color.verde),
                inactiveTrackColor = colorResource(R.color.gris)
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
        "Pagadas" to "Pagada",
        "Anuladas" to "Anulada",
        "Cuota Fija" to "Cuota Fija",
        "Pendientes de pago" to "Pendiente de pago",
        "Plan de pago" to "Plan de pago"
    )

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 20.dp)
    ) {
        Text(
            stringResource(R.string.por_estado),
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
            ) {
                Checkbox(
                    checked = estadosSeleccionados[real] == true,
                    onCheckedChange = { estadosSeleccionados[real] = it }
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
                containerColor = colorResource(R.color.verde),
                contentColor = colorResource(R.color.white)
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.aplicar))
        }

        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.gris)
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.elimina_filtros))
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