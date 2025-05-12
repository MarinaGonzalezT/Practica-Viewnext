package com.viewnext.kotlinmvvm.core.ui.screens.facturas

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.core.ui.FacturasUiState
import com.viewnext.kotlinmvvm.core.ui.screens.ErrorScreen
import com.viewnext.kotlinmvvm.core.ui.screens.LoadingScreen
import com.viewnext.kotlinmvvm.core.ui.screens.PopUps
import com.viewnext.kotlinmvvm.core.ui.screens.Titulo
import com.viewnext.kotlinmvvm.core.ui.viewmodels.FacturasViewModel
import com.viewnext.kotlinmvvm.domain.model.Factura
import java.text.SimpleDateFormat

@Composable
fun PantallaFacturas(
    navController: NavController
) {
    val viewModel : FacturasViewModel = hiltViewModel()
    val facturasUiState by viewModel.facturasUiState.collectAsState()

    Scaffold(
        topBar = {
            FacturasTopBar(
                onBack = { navController.popBackStack("Inicio", inclusive = false) },
                onFilter = { navController.navigate("Filtros")}
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Titulo(stringResource(R.string.Inicio_facturas))
            Spacer(modifier = Modifier.height(10.dp))
            FacturasDeciderScreen(
                facturasUiState = facturasUiState,
                retryAction = viewModel::cargarFacturas
            )
        }
    }
}

@Composable
fun FacturasDeciderScreen(
    facturasUiState: FacturasUiState,
    retryAction: () -> Unit
) {
    Crossfade(
        targetState = facturasUiState,
        animationSpec = tween(1000)
    ) { estado ->
        when(estado) {
            is FacturasUiState.Loading -> LoadingScreen()
            is FacturasUiState.Succes -> {
                if(estado.facturas.isEmpty()) {
                    SinFacturasScreen()
                } else {
                    LazyColumn {
                        items(estado.facturas) { factura -> ItemFactura(factura) }
                    }
                }
            }
            is FacturasUiState.Error -> ErrorScreen(
                retryAction = retryAction,
                mensaje = estado.mensaje,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun SinFacturasScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = stringResource(R.string.Facturas_no_encontraron_facturas),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.gris),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasTopBar(
    onBack: () -> Unit,
    onFilter: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.Facturas_consumo),
                    color = colorResource(R.color.verde)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = colorResource(R.color.verde)
                )
            }
        },
        actions = {
            IconButton(onClick = onFilter) {
                Icon(
                    painter = painterResource(id = R.drawable.filtericon_3x),
                    contentDescription = null,
                    tint = colorResource(R.color.gris)
                )
            }
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun ItemFactura(
    factura: Factura
) {
    var facturaSeleccionada by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { facturaSeleccionada = true }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formatearFecha(factura.fecha),
                fontSize = 22.sp
            )
            if(factura.estado == "Pendiente de pago") {
                Text(
                    text = stringResource(R.string.Facturas_Estados_pendiente_pago),
                    fontSize = 20.sp,
                    color = colorResource(R.color.rojo)
                )
            }
        }

        Text(
            text = "${String.format("%.2f", factura.importe)} €",
            fontSize = 22.sp,
            modifier = Modifier
                .padding(end = 8.dp, top = 16.dp, bottom = 16.dp)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = colorResource(R.color.gris)
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

    if(facturaSeleccionada) {
        PopUps(
            onClick = { facturaSeleccionada = false },
            titulo = stringResource(R.string.General_informacion),
            mensaje = stringResource(R.string.Facturas_texto_popup_facturas),
            textoBoton = stringResource(R.string.General_cerrar)
        )
    }
}

@Composable
private fun formatearFecha(fechaOriginal: String): String {
    val locale = LocalContext.current.resources.configuration.locales[0]

    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", locale)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", locale)
        val fecha = inputFormat.parse(fechaOriginal)
        val fechaFormateada = outputFormat.format(fecha!!)

        val partes = fechaFormateada.split(" ")
        val dia = partes[0]
        val mes = partes[1].replaceFirstChar { it.uppercaseChar() }
        val anio = partes[2]

        "$dia $mes $anio"
    } catch (e: Exception) {
        fechaOriginal
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBarFactura() {
    FacturasTopBar(
        onBack = {},
        onFilter = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemFactura1() {
    val facturaPrueba1 = Factura(0, "Pendiente de pago", 56.38, "22 Jun 2020")
    ItemFactura(facturaPrueba1)
}

@Preview(showBackground = true)
@Composable
fun PreviewItemFactura2() {
    val facturaPrueba2 = Factura(1, "Pagada", 56.38, "22 Jun 2020")
    ItemFactura(facturaPrueba2)
}

@Preview(showBackground = true)
@Composable
fun PreviewPopUpFacturas() {
    PopUps(
        onClick = {},
        titulo = stringResource(R.string.General_informacion),
        mensaje = stringResource(R.string.Facturas_texto_popup_facturas),
        textoBoton = stringResource(R.string.General_cerrar)
    )
}