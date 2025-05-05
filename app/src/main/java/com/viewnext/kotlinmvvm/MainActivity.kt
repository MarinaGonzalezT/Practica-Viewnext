package com.viewnext.kotlinmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.core.ui.viewmodels.FacturasViewModel
import com.viewnext.kotlinmvvm.core.ui.screens.facturas.PantallaFacturas
import com.viewnext.kotlinmvvm.core.ui.screens.facturas.PantallaFiltros
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaInicio
import com.viewnext.kotlinmvvm.core.ui.screens.smartSolar.PantallaSmartSolar
import com.viewnext.kotlinmvvm.core.ui.theme.KotlinMVVMTheme
import com.viewnext.kotlinmvvm.data_retrofit.di.MockProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            KotlinMVVMTheme {
                NavHost(navController = navController, startDestination = "Inicio") {
                    composable("Inicio") {
                        PantallaInicio(
                            navController = navController,
                            onClick = {
                                MockProvider.alternarMock()
                                FacturasViewModel.datosCargados = false
                            }
                        )
                    }
                    composable("Facturas") {
                        PantallaFacturas(
                            navController = navController
                        )
                    }
                    composable("Filtros") {
                        PantallaFiltros(
                            navController = navController
                        )
                    }
                    composable("Smart_Solar") {
                        PantallaSmartSolar(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}