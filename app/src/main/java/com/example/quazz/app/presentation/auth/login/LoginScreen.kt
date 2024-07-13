package com.example.quazz.app.presentation.auth.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.R
import com.example.quazz.core.components.QuazzSnackbar
import com.example.quazz.core.components.QuazzTopAppBar
import com.example.quazz.core.components.outlinedTextField.OutlinedTextFieldEmail
import com.example.quazz.core.components.outlinedTextField.OutlinedTextFieldPassword
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
){
    val state by viewModel.uiState.collectAsState()
    val errorMessage = state.signinError.asString()
    Scaffold(
        topBar = {
            QuazzTopAppBar(
                isLoading = state.isLoading
            )
        },
    ){
        LoginContent(
            modifier = Modifier.padding(it),
            onEvent = viewModel::onEvent,
            state = state,
            openAndPopUp = openAndPopUp,
            openScreen = openScreen)
        QuazzSnackbar(
            paddingValues = it,
            snackbarMessage = errorMessage,
            onDismissed = { viewModel.onEvent(LoginEvent.ClearSignInError) }
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    onEvent: (LoginEvent) -> Unit,
    state: LoginState,
    openAndPopUp: (String, String) -> Unit,
    openScreen: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(horizontal = QuazzTheme.dimension.paddingM)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(id = R.string.sign_in), style = MaterialTheme.typography.displayLarge)

        TextError(state.fieldsError.asString())

        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
        OutlinedTextFieldEmail(
            value = state.email,
            onValueChange = { onEvent(LoginEvent.UpdateEmail(it))},
            onIconButtonClick = { onEvent(LoginEvent.UpdateEmail("")) },
            onFocus = { onEvent(LoginEvent.ClearFieldsError) })
        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingXS))
        OutlinedTextFieldPassword(
            value = state.password,
            onValueChange = { onEvent(LoginEvent.UpdatePassword(it))},
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            onFocus = { onEvent(LoginEvent.ClearFieldsError) })
        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingM))

        Button(onClick = {
            focusManager.clearFocus()
            onEvent(LoginEvent.SignIn(openAndPopUp))
        }) {
            Text(text = stringResource(id = R.string.sign_in))
        }

        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingM))

        TextButton(onClick = {
            focusManager.clearFocus()
            onEvent(LoginEvent.SignUp(openScreen))
        }) {
            Text(text = stringResource(id = R.string.sign_up))
        }
    }
}

@Composable
fun TextError(errorMessage: String) {
    if (errorMessage.isNotBlank()) {
        Text(
            modifier = Modifier.padding(horizontal = QuazzTheme.dimension.paddingM),
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Start
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@PreviewLightDark
@Composable
fun LoginContentPreview() {
    AppTheme {
        Scaffold {
            LoginContent(
                onEvent = {},
                state = LoginState(),
                openAndPopUp = { _, _ -> },
                openScreen = { _ -> },
            )
        }
    }
}