package com.viewnext.kotlinmvvm.core.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viewnext.kotlinmvvm.R

@Composable
fun DetallesContent() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        CampoInfoDetalles(
            titulo = stringResource(R.string.CAU),
            valor = "ES00210000000001994LJ1FA000"
        )

        EstadoSolicitud(
            titulo = stringResource(R.string.estado_solicitud),
            valor = "No hemos recibido ninguna solicitud de autoconsumo"
        )

        CampoInfoDetalles(
            titulo = stringResource(R.string.tipo_autoconsumo),
            valor = "Con excedentes y compensación individual - Consumo"
        )

        CampoInfoDetalles(
            titulo = stringResource(R.string.compensacion_excedente),
            valor = "Precio PVPC"
        )

        CampoInfoDetalles(
            titulo = stringResource(R.string.potencia_instalacion),
            valor = "5kWp"
        )
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
        PopUpDetalles( onClick = { mostrarPopUp = false } )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpDetalles(
    onClick: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onClick,
        content = {
            Surface(
                color = colorResource(R.color.white),
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(30.dp)
                ) {
                    Text(
                        text = stringResource(R.string.estado_solicitud_autoconsumo),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Text(
                        text = stringResource(R.string.texto_popup_autoconsumo),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.verde)
                        ),
                        modifier = Modifier
                            .width(180.dp)
                    ) {
                        Text(text = stringResource(R.string.aceptar))
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDetalles() {
    DetallesContent()
}

@Preview(showBackground = true)
@Composable
fun PreviewPopUpDetalles() {
    PopUpDetalles(onClick = {})
}