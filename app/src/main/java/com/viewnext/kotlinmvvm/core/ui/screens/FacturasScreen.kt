package com.viewnext.kotlinmvvm.core.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.core.ui.viewmodels.FacturasViewModel
import com.viewnext.kotlinmvvm.domain.Factura
import com.viewnext.kotlinmvvm.domain.facturaPrueba1
import com.viewnext.kotlinmvvm.domain.facturaPrueba2

@Composable
fun PantallaFacturas(
    viewModel: FacturasViewModel,
    navController: NavController
) {
    val facturas by viewModel.facturas.observeAsState(emptyList())

    Scaffold(
        topBar = {
            FacturasTopBar(
                onClick = { navController.navigate("Inicio") },
                onFilter = { navController.navigate("Filtros")}
            )
        },
        containerColor = colorResource(R.color.white)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Titulo(stringResource(R.string.facturas))
            Spacer(modifier = Modifier.height(10.dp))
            facturas.forEach { factura ->
                ItemFactura(factura)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasTopBar(
    onClick: () -> Unit,
    onFilter: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.consumo),
                    color = colorResource(R.color.verde)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
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
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.white)
        )
    )
}

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
                text = factura.fecha,
                fontSize = 22.sp
            )
            if(factura.estado == "Pendiente de pago") {
                Text(
                    text = factura.estado,
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
        PopUpFacturas( onClick = { facturaSeleccionada = false } )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpFacturas(
    onClick: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onClick,
        content = {
            Surface(
                color = colorResource(R.color.white),
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(30.dp)
                ) {
                    Text(
                        text = stringResource(R.string.informacion),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Text(
                        text = stringResource(R.string.texto_popup_facturas),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.rojo_claro)
                        )
                    ) {
                        Text(text = stringResource(R.string.cerrar))
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFacturas() {
//    PantallaFacturas(
//        viewModel = FacturasViewModel(),
//        navController = rememberNavController()
//    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemFactura1() {
    ItemFactura(facturaPrueba1)
}

@Preview(showBackground = true)
@Composable
fun PreviewItemFactura2() {
    ItemFactura(facturaPrueba2)
}

@Preview(showBackground = true)
@Composable
fun PreviewPopUpFacturas() {
    PopUpFacturas(onClick = {})
}