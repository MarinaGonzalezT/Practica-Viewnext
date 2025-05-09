package com.viewnext.kotlinmvvm.core.ui.screens.smartSolar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.core.ui.screens.Titulo
import kotlinx.coroutines.launch

@Composable
fun PantallaSmartSolar(
    navController: NavController
) {
    Scaffold(
        topBar = {
            SmartSolarTopBar(
                onBack = { navController.popBackStack("Inicio", inclusive = false) }
            )
        },
        containerColor = colorResource(R.color.white)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Titulo(stringResource(R.string.Inicio_smart_solar))
            SeleccionItems()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSolarTopBar(
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.General_atras),
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.white)
        )
    )
}

@Composable
fun SeleccionItems() {
    val opciones = listOf(
        stringResource(R.string.SmartSolar_mi_instalacion),
        stringResource(R.string.SmartSolar_energia),
        stringResource(R.string.SmartSolar_detalles)
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { opciones.size }
    )
    val coroutineScope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 0.dp,
        divider = {},
        indicator = { posicion ->
            TabRowDefaults.SecondaryIndicator(
                color = colorResource(R.color.black),
                modifier = Modifier
                    .tabIndicatorOffset(posicion[pagerState.currentPage])
                    .height(2.dp)
            )
        },
        containerColor = colorResource(R.color.white),
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ) {
        opciones.forEachIndexed { indice, texto ->
            Tab(
                text = { Text(texto) },
                selected = pagerState.currentPage == indice,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(indice)
                    }
                },
                selectedContentColor = colorResource(R.color.black),
                unselectedContentColor = Color.Gray
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { index ->
        when (index) {
            0 -> MiInstalacionContent()
            1 -> EnergiaContent()
            2 -> DetallesContent()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    SmartSolarTopBar(
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaSS() {
    PantallaSmartSolar(navController = rememberNavController())
}
