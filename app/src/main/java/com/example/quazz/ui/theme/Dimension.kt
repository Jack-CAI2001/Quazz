package com.example.quazz.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class QuazzDimension(
    val paddingXXS: Dp = 2.dp,
    val paddingXS: Dp = 4.dp,
    val paddingS: Dp = 8.dp,
    val paddingM: Dp = 16.dp,
    val paddingL: Dp = 24.dp,
    val paddingXL: Dp = 32.dp,
    val paddingXXL: Dp = 64.dp,
)

val LocalQuazzDimension = staticCompositionLocalOf { QuazzDimension() }