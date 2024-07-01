package com.example.quazz.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object CipherTheme {
    val colors: QuazzPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalQuazzPalette.current

    val typography: QuazzTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalQuazzTypography.current
    val dimension: QuazzDimension
        @Composable
        @ReadOnlyComposable
        get() = LocalQuazzDimension.current
}

@Composable
fun QuazzTheme(
    typography: QuazzTypography = CipherTheme.typography,
    dimension: QuazzDimension = CipherTheme.dimension,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val customColorsPalette =
        if (darkTheme) DarkQuazzPalette
        else LightQuazzPalette

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = customColorsPalette.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalQuazzPalette provides customColorsPalette,
        LocalQuazzTypography provides typography,
        LocalQuazzDimension provides dimension
    ) {
        content()
    }
}