package com.example.quazz.app.presentation.profile.updateProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quazz.app.domain.EmailUseCase
import com.example.quazz.app.domain.PasswordUseCase
import com.example.quazz.app.domain.Result
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
class UpdateProfileViewModel @Inject constructor(
    private val emailUseCase: EmailUseCase,
    private val passwordUseCase: PasswordUseCase,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val confirmPasswordValidator: ConfirmPasswordValidator,

): ViewModel() {

    private val _uiState = MutableStateFlow(UpdateProfileState())
    val uiState: StateFlow<UpdateProfileState> = _uiState.asStateFlow()

    fun onEvent(event: UpdateProfileEvent) {
        when(event) {
            is UpdateProfileEvent.Save -> onSave(event.model, event.onSaveClick)
            is UpdateProfileEvent.UpdateEmail -> updateEmail(event.email)
            is UpdateProfileEvent.UpdateNewPassword -> updateNewPassword(event.newPassword)
            is UpdateProfileEvent.UpdateCurrentPassword -> updateCurrentPassword(event.currentPassword)
            is UpdateProfileEvent.UpdateConfirmPassword -> updateConfirmPassword(event.confirmPassword)
            is UpdateProfileEvent.ClearErrorMessage -> updateErrorMessage(UiText.DynamicString(""))
        }
    }

    private fun onSave(model: UpdateProfile, onSaveClick: () -> Unit, ) {
        when(model) {
            UpdateProfile.UpdateEmail -> {
                changeEmail(onSaveClick)
            }
            UpdateProfile.UpdatePassword -> {
                changePassword(onSaveClick)
            }
        }
    }

    private fun changeEmail(onSaveClick: () -> Unit) {
        emailValidator.execute(_uiState.value.email).let {
            if (it is Result.Error) {
                updateErrorMessage(it.asErrorUiText())
                return
            }
        }
        viewModelScope.launch {
            when(val result = emailUseCase(_uiState.value.email, _uiState.value.currentPassword)) {
                is Result.Error -> {
                    updateErrorMessage(result.asErrorUiText())
                }
                is Result.Success -> {
                    updateEmail("")
                    updateCurrentPassword("")

                    onSaveClick()
                }
            }
        }
    }

    private fun changePassword(onSaveClick: () -> Unit) {
        passwordValidator.execute(_uiState.value.newPassword).let {
            if (it is Result.Error) {
                updateErrorMessage(it.asErrorUiText())
                return
            }
        }
        confirmPasswordValidator.execute(_uiState.value.newPassword, _uiState.value.confirmPassword).let {
            if (it is Result.Error) {
                updateErrorMessage(it.asErrorUiText())
                return
            }
        }
        viewModelScope.launch {
            when(val result = passwordUseCase(_uiState.value.newPassword, _uiState.value.currentPassword)) {
                is Result.Error -> {
                    updateErrorMessage(result.asErrorUiText())
                }
                is Result.Success -> {
                    updateNewPassword("")
                    updateCurrentPassword("")
                    updateConfirmPassword("")
                    onSaveClick()
                }
            }
        }
    }

    private fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    private fun updateNewPassword(newPassword: String) {
        _uiState.value = _uiState.value.copy(newPassword = newPassword)
    }

    private fun updateCurrentPassword(password: String) {
        _uiState.value = _uiState.value.copy(currentPassword = password)
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    private fun updateErrorMessage(errorMessage: UiText) {
        _uiState.value = _uiState.value.copy(errorMessage = errorMessage)
    }

    // endregion
}