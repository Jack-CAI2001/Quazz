package com.example.quazz.app.presentation.auth.register

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.SignUpUseCase
import com.example.quazz.app.domain.asErrorUiText
import com.example.quazz.app.domain.validator.ConfirmPasswordValidator
import com.example.quazz.app.domain.validator.EmailValidator
import com.example.quazz.app.domain.validator.PasswordValidator
import com.example.quazz.app.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val passwordValidator: PasswordValidator,
    private val confirmPasswordValidator: ConfirmPasswordValidator,
    private val emailValidator: EmailValidator
): ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    private val emptyUiText: UiText = UiText.DynamicString("")

    fun onEvent(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.UpdateEmail -> updateEmail(event.email)
            is RegisterEvent.SignUp -> onSignUpClick()
            is RegisterEvent.UpdateConfirmPassword -> updateConfirmPassword(event.confirmPassword)
            is RegisterEvent.UpdatePassword -> updatePassword(event.password)
            is RegisterEvent.ClearPasswordError -> updatePasswordError(emptyUiText)
            is RegisterEvent.ClearEmailError -> updateEmailError(emptyUiText)
            is RegisterEvent.ClearConfirmPasswordError -> updateConfirmPasswordError(emptyUiText)
            is RegisterEvent.ClearSignUpError -> updateSignUpError(emptyUiText)
        }
    }

    // region update state

    private fun updateEmailError(emailError: UiText) {
        _uiState.value = _uiState.value.copy(emailError = emailError)
    }
    private fun updatePasswordError(passwordError: UiText) {
        _uiState.value = _uiState.value.copy(passwordError = passwordError)
    }
    private fun updateConfirmPasswordError(confirmPasswordError: UiText) {
        _uiState.value = _uiState.value.copy(confirmPasswordError = confirmPasswordError)
    }

    @VisibleForTesting
    fun updateLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(loading = isLoading)
    }

    private fun updateEmail(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    private fun updatePassword(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    private fun updateConfirmPassword(newConfirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newConfirmPassword)
    }

    private fun updateSignUpError(signUpError: UiText) {
        _uiState.value = _uiState.value.copy(signUpError = signUpError)
    }

    // endregion
    private fun onSignUpClick() {
        val passwordResult = passwordValidator.execute(_uiState.value.password)
        val confirmPasswordResult = confirmPasswordValidator.execute(_uiState.value.password, _uiState.value.confirmPassword)
        val emailResult = emailValidator.execute(_uiState.value.email)

        val hasError = handleValidator(
            passwordResult to { error -> updatePasswordError(error) },
            confirmPasswordResult to { error -> updateConfirmPasswordError(error) },
            emailResult to { error -> updateEmailError(error) }
        )

        if (hasError) return
        viewModelScope.launch {
            updateLoading(true)
            when (val result = signUpUseCase.invoke(_uiState.value.email, _uiState.value.password)) {
                is Result.Success -> {
                    updateLoading(false)
                    updateEmail("")
                    updatePassword("")
                    updateConfirmPassword("")
                }
                is Result.Error -> {
                    updateLoading(false)
                    val errorMessage = result.asErrorUiText()
                    updateSignUpError(errorMessage)
                }
            }
        }
    }

    private fun handleValidator(vararg validations: Pair<Result<*, Error>, (UiText) -> Unit>): Boolean {
        var hasError = false
        validations.forEach { (result, updateState) ->
            if (result is Result.Error) {
                updateState(result.asErrorUiText())
                hasError = true
            }
        }
        return hasError
    }
}