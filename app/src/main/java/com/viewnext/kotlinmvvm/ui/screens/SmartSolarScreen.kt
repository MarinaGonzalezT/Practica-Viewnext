package com.viewnext.kotlinmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.viewnext.kotlinmvvm.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaSmartSolar(
    navController: NavController
) {
    Scaffold(
        topBar = {
            SmartSolarTopBar(
                onClick = {  }
            )
        }
    ) {
        Titulo(stringResource(R.string.smart_solar))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSolarTopBar(
    onClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.atras),
                    color = colorResource(R.color.verde)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = colorResource(R.color.verde)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    SmartSolarTopBar(
        onClick = {}
    )
}