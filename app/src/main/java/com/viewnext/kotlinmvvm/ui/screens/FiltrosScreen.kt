package com.viewnext.kotlinmvvm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaFiltros(
    navController: NavController,
    minImporte: Float,
    maxImporte: Float
) {
    Scaffold(
        topBar = {
            FiltrosTopBar(
                onClick = { navController.navigate("Facturas") },
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

            SeccionFechas()
            
            SeccionImporte(
                minImporte = minImporte,
                maxImporte = maxImporte
            )

            SeccionChecks()

            SeccionBotones(onApply = {}, onDelete = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosTopBar(
    onClick: () -> Unit
) {
    TopAppBar(
        title = {  },
        actions = {
            IconButton(onClick = onClick) {
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
fun SeccionFechas() {
    var fechaDesde by remember { mutableStateOf<Long?>(null) }
    var fechaHasta by remember { mutableStateOf<Long?>(null) }
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Desde:",
                    color = colorResource(R.color.gris),
                    style = MaterialTheme.typography.bodyMedium
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.medium)
                        .background(color = colorResource(R.color.gris))
                        .clickable { mostrandoPickerPara = "desde" }
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = fechaDesde?.let { dateFormatter.format(Date(it)) } ?: stringResource(R.string.dia_mes_año),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hasta:",
                    color = colorResource(R.color.gris),
                    style = MaterialTheme.typography.bodyMedium
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.medium)
                        .background(color = colorResource(R.color.gris))
                        .clickable { mostrandoPickerPara = "hasta" }
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = fechaHasta?.let { dateFormatter.format(Date(it)) } ?: stringResource(R.string.dia_mes_año),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

    if(mostrandoPickerPara != null) {
        FechaPicker(
            onDateSelected = { millis ->
                when(mostrandoPickerPara) {
                    "desde" -> fechaDesde = millis
                    "hasta" -> fechaHasta = millis
                }
                mostrandoPickerPara = null
            },
            onDismiss = { mostrandoPickerPara = null }
        )
    }
}

@Composable
fun SeccionImporte(
    minImporte: Float,
    maxImporte: Float
) {
    var importe by remember { mutableStateOf(50f..250f) }

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
            onValueChange = { importe = it },
            valueRange = minImporte..maxImporte,
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
fun SeccionChecks() {
    val estados = listOf(
        "Pagadas", "Anuladas", "Cuota Fija", "Pendientes de pago", "Plan de pago"
    )
    val estadosSeleccionados = remember {
        mutableStateMapOf<String, Boolean>().apply { estados.forEach { put(it, false) } }
    }

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

        estados.forEach { estado ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .size(height = 36.dp, width = 200.dp)
            ) {
                Checkbox(
                    checked = estadosSeleccionados[estado] == true,
                    onCheckedChange = { estadosSeleccionados[estado] = it }
                )
                Text(text = estado)
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
        navController = rememberNavController(),
        minImporte = 1f,
        maxImporte = 300f
    )
}