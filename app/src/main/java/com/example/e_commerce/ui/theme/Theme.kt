package com.example.e_commerce.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    secondaryVariant = Color(0xFF018786),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFF000000),
)

private val LightColorScheme = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    secondaryVariant = Color(0xFF018786),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    onError = Color(0xFFFFFFFF),
)
@Composable
fun EcommerceTheme(
    composable: @Composable () -> Unit,
    darkTheme: Boolean = isSystemInDarkTheme()
){
    MaterialTheme (
        colorScheme =  darkTheme,
        content  = composable
        )
}
