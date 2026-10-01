package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TealPrimaryDarkTheme,
    onPrimary = BackgroundDarkTheme,
    primaryContainer = TealDark,
    onPrimaryContainer = TealPrimaryLight,
    secondary = CyanSecondaryDarkTheme,
    onSecondary = BackgroundDarkTheme,
    secondaryContainer = SurfaceVariantDarkTheme,
    onSecondaryContainer = CyanSecondaryLight,
    tertiary = AbscessAlertRed,
    background = BackgroundDarkTheme,
    surface = SurfaceDarkTheme,
    surfaceVariant = SurfaceVariantDarkTheme,
    onBackground = TextPrimaryLightTheme,
    onSurface = TextPrimaryLightTheme,
    onSurfaceVariant = TextSecondaryLightTheme
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealPrimaryLight,
    onPrimaryContainer = TealDark,
    secondary = CyanSecondary,
    onSecondary = Color.White,
    secondaryContainer = CyanSecondaryLight,
    onSecondaryContainer = TealDark,
    tertiary = AbscessAlertRed,
    background = PorcelainBackground,
    surface = PorcelainSurface,
    surfaceVariant = PorcelainSurfaceVariant,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep medical theme consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

