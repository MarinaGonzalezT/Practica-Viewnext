package com.viewnext.kotlinmvvm.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaFacturas
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaFiltros
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaInicio
import com.viewnext.kotlinmvvm.core.ui.screens.PantallaSmartSolar
import com.viewnext.kotlinmvvm.core.ui.theme.KotlinMVVMTheme
import com.viewnext.kotlinmvvm.core.ui.viewmodels.FacturasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: FacturasViewModel = viewModel(
                factory = ViewModelProvider.AndroidViewModelFactory(application)
            )

            KotlinMVVMTheme {
                NavHost(navController = navController, startDestination = "Inicio") {
                    composable("Inicio") {
                        PantallaInicio(navController = navController)
                    }
                    composable("Facturas") {
                        PantallaFacturas(
                            viewModel = FacturasViewModel(application),
                            navController = navController
                        )
                    }
                    composable("Filtros") {
                        PantallaFiltros(
                            navController = navController,
                            minImporte = 1f,
                            maxImporte = 300f
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

@Preview(showBackground = true)
@Composable
fun PreviewPantalla() {
    KotlinMVVMTheme {
        PantallaInicio(navController = rememberNavController())
    }
}