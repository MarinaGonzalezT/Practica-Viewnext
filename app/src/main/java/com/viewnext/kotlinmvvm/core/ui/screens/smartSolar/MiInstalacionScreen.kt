package com.viewnext.kotlinmvvm.core.ui.screens.smartSolar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viewnext.kotlinmvvm.R
import com.viewnext.kotlinmvvm.core.ui.screens.ImagenesSmartSolar

@Composable
fun MiInstalacionContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(color = colorResource(R.color.white))
    ) {
        Text(
            text = stringResource(R.string.SmartSolar_Instalacion_texto_miInstalacion),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoconsumoMiInstalacion()

        Spacer(modifier = Modifier.height(16.dp))

        ImagenesSmartSolar(painter = painterResource(id = R.drawable.mi_instalacion))
    }
}

@Composable
fun AutoconsumoMiInstalacion() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.SmartSolar_Instalacion_autoconsumo),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.gris)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.SmartSolar_Instalacion_porcentaje_autoconsumo),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMiInstalacion() {
    MiInstalacionContent()
}