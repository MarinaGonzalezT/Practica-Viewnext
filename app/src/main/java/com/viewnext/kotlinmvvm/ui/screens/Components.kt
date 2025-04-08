package com.viewnext.kotlinmvvm.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Titulo(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaPicker(
    onClick: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val fechaSeleccionadaState = rememberDatePickerState()

//    DatePickerDialog(
//        onDismissRequest = onDismiss,
//        botonConfirmacion = {
//            TextButton(
//                onClick = {
//                    onClick(fechaSeleccionadaState.selectedDateMillis)
//                    onDismiss
//                }
//            ) {
//                Text("OK")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancelar")
//            }
//        }
//    ) {
//        DatePicker(state = fechaSeleccionadaState)
//    }
}