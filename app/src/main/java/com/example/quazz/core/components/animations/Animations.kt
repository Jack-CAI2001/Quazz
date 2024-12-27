package com.example.quazz.core.components.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun expandFade(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = 100,
            easing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f),
        ),
    ) + expandHorizontally(
        animationSpec = tween(
            durationMillis = 500,
            easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f),
        ),
        expandFrom = Alignment.Start,
    )
}

@Composable
fun fadeOutAndShrink() = fadeOut(
    animationSpec = tween(
        durationMillis = 100,
        easing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f),
    )
) + shrinkHorizontally(
    animationSpec = tween(
        durationMillis = 500,
        easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f),
    ),
    shrinkTowards = Alignment.Start,
)