package com.example.quazz.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quazz.app.domain.GetUserUseCase
import com.example.quazz.app.domain.LogOutUseCase
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.UpdateUserUseCase
import com.example.quazz.app.domain.UserConnectedUseCase
import com.example.quazz.app.domain.asErrorUiText
import com.example.quazz.app.model.User
import com.example.quazz.app.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userConnectedUseCase: UserConnectedUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        isUserConnected()
        getUser()
    }

    fun onEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.LogOut -> onLogOutClick()
            is ProfileEvent.UpdatePseudo -> updatePseudoText(event.pseudo)
            is ProfileEvent.SavePseudo -> savePseudo()
            is ProfileEvent.ClearError -> updateError(UiText.DynamicString(""))
        }
    }

    // region update state

    private fun updateLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }
    private fun updateIsUserConnected(isUserConnected: Boolean) {
        _uiState.value = _uiState.value.copy(isUserConnected = isUserConnected)
    }

    private fun updatePseudoText(pseudoText: String) {
        _uiState.value = _uiState.value.copy(pseudoText = pseudoText)
    }

    private fun updateUser(user: User) {
        _uiState.value = _uiState.value.copy(user = user)
    }

    private fun updateError(error: UiText) {
        _uiState.value = _uiState.value.copy(errorMessage = error)
    }
    // endregion
    private fun isUserConnected() {
        if (userConnectedUseCase.invoke().not()) {
            updateIsUserConnected(false)
            return
        }
        updateIsUserConnected(true)
    }

    private fun onLogOutClick() {
        logOutUseCase.invoke()
        updateIsUserConnected(false)
    }

    private fun getUser() {
        viewModelScope.launch {
            updateLoading(true)
            when (val result = getUserUseCase.invoke()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(user = result.data)
                    updateLoading(false)
                }
                is Result.Error -> {
                    updateLoading(false)
                    updateError(result.asErrorUiText())
                }
            }
        }
    }

    private fun savePseudo() {
        viewModelScope.launch {
            updateLoading(true)
            when(val result = updateUserUseCase.invoke(_uiState.value.user.copy(pseudo = _uiState.value.pseudoText))) {
                is Result.Success -> {
                    updateUser(_uiState.value.user.copy(pseudo = _uiState.value.pseudoText))
                    updateLoading(false)
                }
                is Result.Error -> {
                    updateLoading(false)
                    updateError(result.asErrorUiText())
                }
            }
        }
    }
}