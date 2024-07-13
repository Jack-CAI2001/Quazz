package com.example.quazz.core.components.outlinedTextField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.quazz.R

@Composable
fun OutlinedTextFieldPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    onFocus: () -> Unit = {}
) {
    var showPassword by remember { mutableStateOf(false) }
    QuazzOutlinedTextField(
        onFocus = onFocus,
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Lock,
                contentDescription = stringResource(id = R.string.icon_lock
                )
            ) },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        trailingIcon = {
            val (iconId, contentDescription) = if (showPassword) {
                Pair(
                    R.drawable.visibility_24,
                    stringResource(id = R.string.icon_visibility)
                )
            } else {
                Pair(
                    R.drawable.visibility_off_24,
                    stringResource(id = R.string.icon_visibility_off)
                )
            }
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    painterResource(id = iconId),
                    contentDescription = contentDescription,
                )
            }
        }
    )
}