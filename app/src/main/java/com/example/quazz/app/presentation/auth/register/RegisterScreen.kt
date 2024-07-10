package com.example.quazz.app.presentation.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.R
import com.example.quazz.core.components.OutlinedTextFieldValidation
import com.example.quazz.core.components.QuazzTopAppBar
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme.dimension

@Composable
fun RegisterScreen(
    navigation: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState()}
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signUpError) {
        if (state.signUpError.asString(context).isNotEmpty()) {
            snackbarHostState.showSnackbar(
                withDismissAction = true,
                message = state.signUpError.asString(context))
            viewModel.onEvent(RegisterEvent.ClearSignUpError)
        }
    }
    Scaffold(
        topBar = {
            QuazzTopAppBar(
                title = stringResource(R.string.sign_up),
                canNavigateBack = true,
                navigateUp = navigation,
                isLoading = state.loading
            )
        },
    ) {
        RegisterContent(
            modifier = Modifier.padding(it),
            onEvent = viewModel::onEvent,
            state = state)
        SnackbarHost(hostState = snackbarHostState, Modifier.padding(it))
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    onEvent: (RegisterEvent) -> Unit,
    state: RegisterState = RegisterState()) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(horizontal = dimension.paddingM)
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(id = R.string.sign_up), style = MaterialTheme.typography.displayLarge)

        Spacer(modifier = Modifier.padding(dimension.paddingS))
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
fun OutlinedTextFieldValidationPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean,
    errorMessage: String,
    onFocus: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextFieldValidation(
        onFocus = onFocus,
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        errorMessage = errorMessage,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Lock,
                contentDescription = stringResource(id = R.string.icon_lock
        )) },
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
                Pair(R.drawable.visibility_off_24,
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
        onFocus = onFocus,
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        errorMessage = errorMessage,
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
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterContentPreview() {
    AppTheme {
        RegisterContent(
            onEvent = {},
        )
    }
}