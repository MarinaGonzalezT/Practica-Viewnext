package com.viewnext.kotlinmvvm.core.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = colorResource(R.color.white)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_iberdrola),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 100.dp, bottom = 100.dp, end = 30.dp, start = 30.dp)
                )

                Text(
                    text = stringResource(R.string.Inicio_msg_inicio),
                    fontSize = 30.sp,
                    color = colorResource(R.color.verde_iberdrola),
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                )

                OpcionesInicio(
                    titulo = stringResource(R.string.Inicio_facturas),
                    icono = painterResource(id = R.drawable.icon_facturas),
                    onClick = { navController.navigate("Facturas") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OpcionesInicio(
                    titulo = stringResource(R.string.Inicio_smart_solar),
                    icono = painterResource(id = R.drawable.icon_smartsolar),
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
    icono: Painter,
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(colorResource(R.color.verde))
                .fillMaxSize()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Image(
                painter = icono,
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(R.color.white)),
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 16.dp)
            )
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
    var isMocking by remember { mutableStateOf(MockProvider.isMocking()) }

    val color = if(isMocking) colorResource(R.color.rojo_claro)
                else colorResource(R.color.verde)
    val icono = if(isMocking) painterResource(R.drawable.icon_retromock)
                else painterResource(R.drawable.icon_nube_internet)
    val texto = if(isMocking) "Retromock"
                else "Retrofit"

    ExtendedFloatingActionButton(
        onClick = {
            onClick()
            isMocking = MockProvider.isMocking()
            val mensaje = if(MockProvider.isMocking()) context.getString(R.string.Inicio_mocks_activos)
                          else context.getString(R.string.Inicio_mocks_desactivos)
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        },
        containerColor = color,
        icon = {
            Image(
                painter = icono,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        },
        text = {
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white)
            )
        },
        modifier = modifier
            .padding(16.dp)
            .width(180.dp)
            .wrapContentHeight()
    )
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