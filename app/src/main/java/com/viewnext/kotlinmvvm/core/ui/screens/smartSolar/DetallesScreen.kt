package com.viewnext.kotlinmvvm.core.ui.screens.smartSolar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.core.ui.screens.LoadingScreen
import com.viewnext.kotlinmvvm.core.ui.screens.PopUps
import com.viewnext.kotlinmvvm.core.ui.viewmodels.DetallesViewModel

@Composable
fun DetallesContent(
    viewModel: DetallesViewModel = viewModel(factory = DetallesViewModel.Factory)
) {
    val detalles = viewModel.detalles

    if(detalles != null) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            CampoInfoDetalles(
                titulo = stringResource(R.string.CAU),
                valor = detalles.cau
            )

            EstadoSolicitud(
                titulo = stringResource(R.string.estado_solicitud),
                valor = detalles.estado
            )

            CampoInfoDetalles(
                titulo = stringResource(R.string.tipo_autoconsumo),
                valor = detalles.tipo
            )

            CampoInfoDetalles(
                titulo = stringResource(R.string.compensacion_excedente),
                valor = detalles.compensacion
            )

            CampoInfoDetalles(
                titulo = stringResource(R.string.potencia_instalacion),
                valor = detalles.potencia
            )
        }
    } else {
        LoadingScreen()
    }
}

@Composable
fun CampoInfoDetalles(
    titulo: String,
    valor: String
) {
    Column {
        Text(
            text = titulo,
            color = colorResource(R.color.gris),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 2.dp)
        )

        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EstadoSolicitud(
    titulo: String,
    valor: String
) {
    var mostrarPopUp by remember { mutableStateOf(false) }

    Column {
        Text(
            text = titulo,
            color = colorResource(R.color.gris),
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .width(345.dp)
            )
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier
                    .clickable { mostrarPopUp = true }
            )
        }
    }
    HorizontalDivider()
    Spacer(modifier = Modifier.height(16.dp))

    if(mostrarPopUp) {
        PopUps(
            onClick = { mostrarPopUp = false },
            titulo = stringResource(R.string.estado_solicitud_autoconsumo),
            mensaje = stringResource(R.string.texto_popup_autoconsumo),
            textoBoton = stringResource(R.string.aceptar),
            colorBoton = colorResource(R.color.verde)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetalles() {
    DetallesContent()
}

@Preview(showBackground = true)
@Composable
fun PreviewPopUpDetalles() {
    PopUps(
        onClick = {},
        titulo = stringResource(R.string.estado_solicitud_autoconsumo),
        mensaje = stringResource(R.string.texto_popup_autoconsumo),
        textoBoton = stringResource(R.string.aceptar),
        colorBoton = colorResource(R.color.verde)
    )
}