package com.viewnext.kotlinmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.ui.screens.PantallaFacturas
import com.viewnext.kotlinmvvm.ui.screens.PantallaFiltros
import com.viewnext.kotlinmvvm.ui.screens.PantallaInicio
import com.viewnext.kotlinmvvm.ui.screens.PantallaSmartSolar
import com.viewnext.kotlinmvvm.ui.theme.KotlinMVVMTheme
import com.viewnext.kotlinmvvm.ui.viewmodels.FacturasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = "Inicio") {
                composable("Inicio") {
                    PantallaInicio(navController = navController)
                }
                composable("Facturas") {
                    PantallaFacturas(
                        viewModel = FacturasViewModel(),
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinMVVMTheme {
        PantallaInicio(navController = rememberNavController())
    }
}