package com.example.quazz.core.components.outlinedTextField

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.quazz.R
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    message: String = stringResource(R.string.loading),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(QuazzTheme.dimension.paddingM))
            Text(
                text = message,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
@PreviewLightDark
fun LoadingScreenPreview() {
    AppTheme {
        LoadingScreen()
    }
}