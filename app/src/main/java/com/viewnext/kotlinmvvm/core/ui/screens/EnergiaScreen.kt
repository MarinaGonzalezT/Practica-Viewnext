package com.viewnext.kotlinmvvm.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viewnext.kotlinmvvm.R

@Composable
fun EnergiaContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(color = colorResource(R.color.white))
    ) {
        ImagenesSmartSolar(painter = painterResource(id = R.drawable.imagen_energia))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.texto_energia),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEnergia() {
    EnergiaContent()
}