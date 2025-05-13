package com.viewnext.kotlinmvvm.core.ui.screens

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.core.ui.screens.facturas.formatearFecha
import com.viewnext.kotlinmvvm.domain.model.Factura
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.ceil

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
    fechaSeleccionada: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    minFecha: Long? = null,
    maxFecha: Long? = null,
    tipo: String
) {
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaSeleccionada
    )
    var mostrarPopUp by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    DatePickerDialog(
        onDismissRequest = {
            mostrarPopUp = false
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                val seleccionado = datePickerState.selectedDateMillis
                val hoy = System.currentTimeMillis()

                when {
                    seleccionado == null -> false
                    seleccionado > hoy -> {
                        mensajeError = context.getString(R.string.Filtros_Validar_Fechas_no_fecha_posterior_hoy)
                        mostrarPopUp = true
                    }
                    tipo == "desde" && maxFecha != null && seleccionado > maxFecha -> {
                        mensajeError = context.getString(R.string.Filtros_Validar_Fechas_no_fecha_inicio_posterior_fin)
                        mostrarPopUp = true
                    }
                    tipo == "hasta" && minFecha != null && seleccionado < minFecha -> {
                        mensajeError = context.getString(R.string.Filtros_Validar_Fechas_no_fecha_fin_anterior_inicio)
                        mostrarPopUp = true
                    }
                    else -> {
                        onDateSelected(seleccionado)
                        onDismiss()
                    }
                }
            }) {
                Text(
                    text = stringResource(R.string.General_aceptar)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.General_cancelar)
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }

    if(mostrarPopUp) {
        PopUps(
            onClick = { mostrarPopUp = false },
            titulo = stringResource(R.string.Filtros_Validar_Fechas_no_fecha_valida),
            mensaje = mensajeError,
            textoBoton = stringResource(R.string.General_aceptar)
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
                .background(color = colorResource(R.color.gris_claro))
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = fecha?.let { dateFormatter.format(Date(it)) } ?: stringResource(R.string.Filtros_dia_mes_año),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.gris_oscuro)
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
    textoBoton: String
) {
    BasicAlertDialog(
        onDismissRequest = onClick,
        content = {
            Surface(
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
                            containerColor = colorResource(R.color.verde)
                        ),
                        modifier = Modifier.width(180.dp)
                    ) {
                        Text(
                            text = textoBoton
                        )
                    }
                }
            }
        }
    )
}

// Componente reutilizable para la pantalla de carga
@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            trackColor = colorResource(R.color.gris),
            color = colorResource(R.color.verde),
            modifier = Modifier.width(64.dp),
        )
    }
}

//Componente reutilizable para la pantalla de error
@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    mensaje: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colorResource(R.color.gris))
        )
        Text(
            text = mensaje,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = retryAction,
            colors = ButtonDefaults.buttonColors(colorResource(R.color.verde))
        ) {
            Text(
                text = stringResource(R.string.General_retry),
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

//Componente para la gráfica de precios
@Composable
fun GraficoPrecios(facturas: List<Factura>) {
    if(facturas.isEmpty()) return

    val valores = facturas.map { it.importe.toDouble() }

    val fechas = facturas.mapNotNull {
        formatearFecha(it.fecha, "LLL.yy")
    }

    val maxImporte = valores.maxOrNull()?.let { ceil(it) } ?: 0.0
    val midImporte = maxImporte / 2

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 22.dp),
        data = remember {
            listOf(
                Line(
                    label = "",
                    values = valores,
                    color = SolidColor(Color(0xFF94BA4D)),
                    firstGradientFillColor = Color(0xFFA8CE61).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(Color(0xFF94BA4D)),
                        strokeColor = SolidColor(Color(0xFF94BA4D)),
                        radius = 3.dp,
                        strokeWidth = 2.dp
                    )
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        labelProperties = LabelProperties(
            enabled = true,
            labels = fechas,
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(enabled = true), // oculta líneas verticales
            yAxisProperties = GridProperties.AxisProperties(enabled = false)   // mantiene líneas horizontales
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            count = IndicatorCount.CountBased(count = 3),
            indicators = listOf(maxImporte, midImporte, 0.0),
            contentBuilder = { value -> "${value.toInt()} €" },
            position = IndicatorPosition.Horizontal.End,
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        ),

        )
}
