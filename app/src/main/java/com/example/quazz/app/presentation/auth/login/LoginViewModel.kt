package com.example.quazz.app.presentation.auth.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.SignInUseCase
import com.example.quazz.app.domain.asErrorUiText
import com.example.quazz.app.presentation.UiText
import com.example.quazz.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.SignUp -> onSignUpClick(event.openScreen)
            is LoginEvent.SignIn -> onSignInClick(event.openAndPopUp)
            is LoginEvent.UpdateEmail -> updateEmail(event.email)
            is LoginEvent.UpdatePassword -> updatePassword(event.password)
            is LoginEvent.ClearSignInError -> updateSigninError(UiText.DynamicString(""))
            is LoginEvent.ClearFieldsError -> updateFieldsError(UiText.DynamicString(""))
        }
    }

    // region update state
    private fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    private fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    private fun updateSigninError(error: UiText) {
        _uiState.value = _uiState.value.copy(signinError = error)
    }

    private fun updateFieldsError(error: UiText) {
        _uiState.value = _uiState.value.copy(fieldsError = error)
    }

    @VisibleForTesting
    fun updateLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    // endregion

    private fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        val email = _uiState.value.email
        val password = _uiState.value.password
        validate(email, password).let {
            if (it is Result.Error) return updateFieldsError(it.asErrorUiText())
        }

        viewModelScope.launch {
            updateLoading(true)
            when(val result = signInUseCase.invoke(email, password)) {
                is Result.Success -> {
                    updateLoading(false)
                    openAndPopUp(Route.AppRoute.route, Route.LoginRoute.route)
                }
                is Result.Error -> {
                    updateLoading(false)
                    updateSigninError(result.asErrorUiText())
                }
            }
        }
    }

    private fun onSignUpClick(openScreen: (String) -> Unit) {
        updateEmail("")
        updatePassword("")
        updateSigninError(UiText.DynamicString(""))
        updateFieldsError(UiText.DynamicString(""))
        openScreen(Route.RegisterRoute.route)
    }

    private fun validate(email: String, password: String): Result<Unit, Error.CommonError> {
        if (email.isBlank() || password.isBlank())
            return Result.Error(Error.CommonError.EMPTY_FIELD)
        return Result.Success(Unit)
    }
}