package com.example.quazz.core.components

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