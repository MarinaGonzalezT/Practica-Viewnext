package com.viewnext.kotlinmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.ui.Factura
import com.viewnext.kotlinmvvm.ui.facturaPrueba1
import com.viewnext.kotlinmvvm.ui.facturaPrueba2
import com.viewnext.kotlinmvvm.ui.facturasPrueba
import com.viewnext.kotlinmvvm.ui.viewmodels.FacturasViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaFacturas(
    viewModel: FacturasViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            FacturasTopBar(
                onClick = { navController.navigate("Inicio") },
                onFilter = { navController.navigate("Filtros")}
            )
        }
    ) {
        Titulo(stringResource(R.string.facturas))
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
                    Icons.Default.KeyboardArrowLeft,
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
        }
    )
}

@Composable
fun ItemFactura(
    factura: Factura,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = factura.fecha,
                style = MaterialTheme.typography.bodyLarge
            )
            if(factura.estado == "Pendiente de pago") {
                Text(
                    text = factura.estado,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.rojo)
                )
            }
        }

        Text(
            text = "${String.format("%.2f", factura.importe)} €",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(end = 8.dp)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = colorResource(R.color.gris)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFacturas() {
    PantallaFacturas(
        viewModel = FacturasViewModel(),
        navController = rememberNavController()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemFactura1() {
    ItemFactura(facturaPrueba1) {}
}

@Preview(showBackground = true)
@Composable
fun PreviewItemFactura2() {
    ItemFactura(facturaPrueba2) {}
}