package com.example.quazz.app.presentation.auth.register

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.R
import com.example.quazz.core.components.OutlinedTextFieldValidation
import com.example.quazz.core.components.QuazzSnackbar
import com.example.quazz.core.components.QuazzTopAppBar
import com.example.quazz.core.components.outlinedTextField.OutlinedTextFieldEmail
import com.example.quazz.core.components.outlinedTextField.OutlinedTextFieldPassword
import com.example.quazz.core.components.outlinedTextField.QuazzOutlinedTextField
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme.dimension

@Composable
fun RegisterScreen(
    navigation: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val errorMessage = state.signUpError.asString()
    Scaffold(
        topBar = {
            QuazzTopAppBar(
                title = stringResource(R.string.sign_up),
                canNavigateBack = true,
                navigateUp = navigation,
                isLoading = state.isLoading
            )
        },
    ) {
        RegisterContent(
            modifier = Modifier.padding(it),
            onEvent = viewModel::onEvent,
            state = state)
        QuazzSnackbar(
            paddingValues = it,
            snackbarMessage = errorMessage,
            onDismissed = { viewModel.onEvent(RegisterEvent.ClearSignUpError) }
        )
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    onEvent: (RegisterEvent) -> Unit,
    state: RegisterState) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(horizontal = dimension.paddingM)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(id = R.string.sign_up), style = MaterialTheme.typography.displayLarge)

        Spacer(modifier = Modifier.padding(dimension.paddingS))

        OutlinedTextFieldValidationPseudo(
            value = state.pseudo,
            onValueChange = { onEvent(RegisterEvent.UpdatePseudo(it)) },
            isError = state.pseudoError.asString().isNotEmpty(),
            errorMessage = state.pseudoError.asString(),
            onFocus = { onEvent(RegisterEvent.ClearPseudoError) }
        )
        Spacer(modifier = Modifier.padding(dimension.paddingXS))

        OutlinedTextFieldValidationEmail(
            value = state.email,
            onValueChange = { onEvent(RegisterEvent.UpdateEmail(it)) },
            isError = state.emailError.asString().isNotEmpty(),
            errorMessage = state.emailError.asString(),
            onIconButtonClick = { onEvent(RegisterEvent.UpdateEmail("")) },
            onFocus = { onEvent(RegisterEvent.ClearEmailError) }
        )
        Spacer(modifier = Modifier.padding(dimension.paddingXS))
        OutlinedTextFieldValidationPassword(
            value = state.password,
            onValueChange = { onEvent(RegisterEvent.UpdatePassword(it)) },
            isError = state.passwordError.asString().isNotEmpty(),
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            errorMessage = state.passwordError.asString(),
            onFocus = { onEvent(RegisterEvent.ClearPasswordError) }
            )
        Spacer(modifier = Modifier.padding(dimension.paddingXS))

        OutlinedTextFieldValidationPassword(
            value = state.confirmPassword,
            onValueChange = { onEvent(RegisterEvent.UpdateConfirmPassword(it)) },
            isError = state.confirmPasswordError.asString().isNotEmpty(),
            label = stringResource(R.string.confirm_password),
            placeholder = stringResource(R.string.confirm_password),
            errorMessage = state.confirmPasswordError.asString(),
            onFocus = { onEvent(RegisterEvent.ClearConfirmPasswordError) }
        )

        Spacer(modifier = Modifier.padding(dimension.paddingM))
        SignUpButton(onClick = {
            focusManager.clearFocus()
            onEvent(RegisterEvent.SignUp)
        })
    }
}

@Composable
fun SignUpButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = dimension.paddingS)
        )
    }
}

@Composable
fun OutlinedTextFieldValidationPseudo(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    onFocus: () -> Unit
) {
    OutlinedTextFieldValidation(
        outlinedTextField = {
            QuazzOutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(id = R.string.edit_pseudo))},
                label = { Text(text = stringResource(id = R.string.edit_pseudo)) },
                placeholder = { Text(text = stringResource(id = R.string.placeholder_pseudo)) },
                isError = isError,
                onFocus = onFocus
            )
        },
        isError = isError,
        errorMessage = errorMessage)
}

@Composable
fun OutlinedTextFieldValidationPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean,
    errorMessage: String,
    onFocus: () -> Unit
) {
    OutlinedTextFieldValidation(
        outlinedTextField = {
            OutlinedTextFieldPassword(
                value = value,
                onValueChange = onValueChange,
                label = label,
                placeholder = placeholder,
                isError = isError,
                onFocus = onFocus
            )
        },
        isError = isError,
        errorMessage = errorMessage
    )
}

@Composable
fun OutlinedTextFieldValidationEmail(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    onIconButtonClick: () -> Unit,
    onFocus: () -> Unit
) {
    OutlinedTextFieldValidation(
        outlinedTextField =
        {
        OutlinedTextFieldEmail(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            onFocus = onFocus,
            onIconButtonClick = { onIconButtonClick() })
        },
        isError = isError,
        errorMessage = errorMessage
    )
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@PreviewLightDark
@Composable
fun RegisterContentPreview() {
    AppTheme {
        Scaffold {
            RegisterContent(
                onEvent = {},
                state = RegisterState()
            )
        }
    }
}