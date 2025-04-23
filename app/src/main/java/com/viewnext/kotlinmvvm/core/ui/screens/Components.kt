package com.viewnext.kotlinmvvm.core.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.kotlinmvvm.R
import java.text.SimpleDateFormat
import java.util.Date

// Componente reutilizable para los títulos de las pantallas
@Composable
fun Titulo(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        modifier = Modifier
            .padding(horizontal = 12.dp)
    )
}

// Componente reutilizable para el cuadro de las fechas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaPicker(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = colorResource(R.color.white)
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = colorResource(R.color.white)
            )
        )
    }
}

// Componente reutilizable para mostrar las fechas en el recuadro de la pantalla de filtros
@Composable
fun CuadroFechas(
    etiqueta: String,
    fecha: Long?,
    onClick: () -> Unit,
    dateFormatter: SimpleDateFormat,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = etiqueta,
            color = colorResource(R.color.gris),
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = colorResource(R.color.gris))
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = fecha?.let { dateFormatter.format(Date(it)) } ?: stringResource(R.string.dia_mes_año),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
    }
}

// Componenete reutilizable para las imágenes de "Mi instalación" y "Energía"
@Composable
fun ImagenesSmartSolar(
    painter: Painter
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(1f)
    )
}

// Componente reutilizable para los popUps de "Facturas" y "Detalles"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUps(
    onClick: () -> Unit,
    titulo: String,
    mensaje: String,
    textoBoton: String,
    colorBoton: Color
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
                    modifier = Modifier.padding(30.dp)
                ) {
                    Text(
                        text = titulo,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Text(
                        text = mensaje,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorBoton
                        ),
                        modifier = Modifier.width(180.dp)
                    ) {
                        Text(text = textoBoton)
                    }
                }
            }
        }
    )
}
