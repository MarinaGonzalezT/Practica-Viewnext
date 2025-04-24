package com.viewnext.kotlinmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.core.ui.FacturasViewModel
import com.viewnext.kotlinmvvm.core.ui.FiltrosViewModel
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaFacturas
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaFiltros
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaInicio
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaSmartSolar
import com.viewnext.kotlinmvvm.core.ui.theme.KotlinMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val facturasviewModel : FacturasViewModel = viewModel(factory = FacturasViewModel.Factory)
            val filtrosViewModel : FiltrosViewModel = viewModel(factory = FiltrosViewModel.provideFactory(facturasviewModel))

            val estadoFiltro = filtrosViewModel.filtros.collectAsState()

            KotlinMVVMTheme {
                NavHost(navController = navController, startDestination = "Inicio") {
                    composable("Inicio") {
                        PantallaInicio(navController = navController)
                    }
                    composable("Facturas") {
                        PantallaFacturas(
                            navController = navController,
                            viewModel = facturasviewModel
                        )
                    }
                    composable("Filtros") {
                        PantallaFiltros(
                            navController = navController,
                            minImporte = estadoFiltro.value.importeMin,
                            maxImporte = estadoFiltro.value.importeMax,
                            viewModel = filtrosViewModel
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