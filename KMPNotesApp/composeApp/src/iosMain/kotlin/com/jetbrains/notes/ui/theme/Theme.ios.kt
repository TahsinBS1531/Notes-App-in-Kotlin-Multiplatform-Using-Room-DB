package com.example.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.ui.theme.AppTypography

@Composable
actual fun isDarkThemeEnabled(): Boolean {
    return isSystemInDarkTheme()
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isDarkThemeEnabled(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}