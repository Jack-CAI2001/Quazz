package com.example.quazz.core.components

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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