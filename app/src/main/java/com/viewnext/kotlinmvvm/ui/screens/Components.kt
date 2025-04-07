package com.viewnext.kotlinmvvm.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Titulo(titulo: String) {
    Text(
        text = titulo,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}