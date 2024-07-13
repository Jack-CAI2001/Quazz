package com.example.quazz.core.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.quazz.core.components.outlinedTextField.QuazzOutlinedTextField
import com.example.quazz.ui.theme.QuazzTheme.dimension

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OutlinedTextFieldValidation(
    outlinedTextField: @Composable () -> Unit,
    isError: Boolean = false,
    errorMessage: String
) {
    Column {
        outlinedTextField()
        if (isError) {
            Text(
                modifier = Modifier.padding(horizontal = dimension.paddingM),
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun OutlinedTextFieldValidationPreview() {
    Background {
        OutlinedTextFieldValidation(
            outlinedTextField =
            {
                QuazzOutlinedTextField(
                    isError = true,
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = "Placeholder") },
                )
            },
            isError = true,
            errorMessage = "ErrorMessage",
        )
    }
}