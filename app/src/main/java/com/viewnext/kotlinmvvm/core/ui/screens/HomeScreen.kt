package com.viewnext.kotlinmvvm.core.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viewnext.kotlinmvvm.BuildConfig
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.data_retrofit.di.MockProvider

@Composable
fun PantallaInicio(
    navController: NavController,
    onClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = colorResource(R.color.white)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_iberdrola),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 100.dp, bottom = 100.dp, end = 30.dp, start = 30.dp)
                )

                Text(
                    text = "¡Estás de vuelta!",
                    fontSize = 30.sp,
                    color = colorResource(R.color.verde_iberdrola),
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                )

                OpcionesInicio(
                    titulo = stringResource(R.string.facturas),
                    onClick = { navController.navigate("Facturas") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OpcionesInicio(
                    titulo = stringResource(R.string.smart_solar),
                    onClick = { navController.navigate("Smart_Solar") }
                )
            }
        }

        if(BuildConfig.DEBUG) {
            BotonRetromock(
                onClick = { onClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun OpcionesInicio(
    titulo: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.verde)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clickable { onClick() }
            .padding(4.dp)
            .size(width = 400.dp, height = 90.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(colorResource(R.color.verde))
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.displayMedium,
                fontSize = 36.sp,
                color = colorResource(R.color.white)
            )
        }
    }
}

@Composable
private fun BotonRetromock(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .clickable {
                onClick()
                val mensaje = if (MockProvider.isMocking()) "Mocks activados" else "Mocks desactivados"
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_retromock),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = stringResource(R.string.retromock),
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = colorResource(R.color.white),
                    offset = Offset(0f, 0f),
                    blurRadius = 8f
                )
            ),
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBotonRetromock() {
    BotonRetromock(onClick = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    PantallaInicio(
        navController = rememberNavController(),
        onClick = {}
    )
}