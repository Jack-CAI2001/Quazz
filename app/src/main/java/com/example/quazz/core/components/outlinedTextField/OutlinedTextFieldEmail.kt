package com.example.quazz.core.components.outlinedTextField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.quazz.R

@Composable
fun OutlinedTextFieldEmail(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    onIconButtonClick: () -> Unit,
    onFocus: () -> Unit = {}
) {
    QuazzOutlinedTextField(
        onFocus = onFocus,
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(id = R.string.email)) },
        keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Email),
        label = { Text(text = stringResource(id = R.string.email)) },
        placeholder = { Text(text = stringResource(id = R.string.placeholder_email)) },
        trailingIcon = {
            IconButton(onClick = { onIconButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(id = R.string.icon_clear),
                )
            }
        }
    )
}