package com.example.quazz.core.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle


@Composable
fun TextTitle(title: String, style: TextStyle = MaterialTheme.typography.titleMedium) {
    Text(text = title, style = style)
}

@Composable
fun SubText(title: String, style: TextStyle = MaterialTheme.typography.bodySmall) {
    Text(text = title, style = style)
}

fun slideInFromRightAndSlideOutToLeft(): ContentTransform {
    return slideInHorizontally{ it } + fadeIn() togetherWith slideOutHorizontally{ -it } + fadeOut()
}

fun slideInFromLeftAndSlideOutToRight(): ContentTransform {
    return slideInHorizontally{ -it } + fadeIn() togetherWith slideOutHorizontally{ it } + fadeOut()
}

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope> {
    error("Must be provided first")
}

@Composable
fun AnimatedVisibilityScope.ProvideAnimatedVisibilityScope(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAnimatedVisibilityScope provides this,
        content = content
    )
}

@Composable
fun WithAnimatedVisibilityScope(block: @Composable AnimatedVisibilityScope.() -> Unit) {
    with(LocalAnimatedVisibilityScope.current) {
        block()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    error("Must be provided first")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProvideSharedTransitionScope(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSharedTransitionScope provides this,
        content = content
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WithSharedTransitionScope(block: @Composable SharedTransitionScope.() -> Unit) {
    with(LocalSharedTransitionScope.current) {
        block()
    }
}