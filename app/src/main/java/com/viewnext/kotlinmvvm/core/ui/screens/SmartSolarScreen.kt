package com.viewnext.kotlinmvvm.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.R

@Composable
fun PantallaSmartSolar(
    navController: NavController
) {
    Scaffold(
        topBar = {
            SmartSolarTopBar(
                onClick = { navController.navigate("Inicio") }
            )
        },
        containerColor = colorResource(R.color.white)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Titulo(stringResource(R.string.smart_solar))
            SeleccionItems()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSolarTopBar(
    onClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.atras),
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.white)
        )
    )
}

@Composable
fun SeleccionItems() {
    val opciones = listOf(
        stringResource(R.string.mi_instalacion),
        stringResource(R.string.energia),
        stringResource(R.string.detalles)
    )
    var selectedTadIndex by remember { mutableStateOf(-1) }

    ScrollableTabRow(
        selectedTabIndex = selectedTadIndex.coerceAtLeast(0),
        edgePadding = 0.dp,
        divider = {},
        indicator = { posicion ->
            if(selectedTadIndex >= 0) {
                TabRowDefaults.SecondaryIndicator(
                    color = colorResource(R.color.black),
                    modifier = Modifier
                        .tabIndicatorOffset(posicion[selectedTadIndex])
                        .height(2.dp)
                )
            }
        },
        containerColor = colorResource(R.color.white),
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ) {
        opciones.forEachIndexed { indice, texto ->
            Tab(
                text = { Text(texto) },
                selected = selectedTadIndex == indice,
                onClick = { selectedTadIndex = indice },
                selectedContentColor = colorResource(R.color.black),
                unselectedContentColor = Color.Gray
            )
        }
    }

    when (selectedTadIndex) {
        0 -> MiInstalacionContent()
        1 -> EnergiaContent()
        2 -> DetallesContent()
        else -> {}
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    SmartSolarTopBar(
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaSS() {
    PantallaSmartSolar(navController = rememberNavController())
}