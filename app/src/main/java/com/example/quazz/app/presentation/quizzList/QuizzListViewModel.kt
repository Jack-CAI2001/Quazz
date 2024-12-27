package com.example.quazz.app.presentation.quizzList

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quazz.app.domain.Result
import com.example.quazz.app.repository.AuthRepository
import com.example.quazz.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizzListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizzListState())
    val uiState: StateFlow<QuizzListState> = _uiState.asStateFlow()

    init {
        getUserCreatedQuizzList()
    }

    private fun getUserCreatedQuizzList() {
        viewModelScope.launch {
            updateLoading(true)
            when(val result = userRepository.getQuizzCreated(authRepository.currentUserUid)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(quizzList = result.data)
                    updateLoading(false)
                }
                is Result.Error -> {
                    updateLoading(false)
                }
            }
        }
    }

    private fun getUserQuizzList() {
        viewModelScope.launch {
            updateLoading(true)
            when(val result = userRepository.getQuizzList(authRepository.currentUserUid)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(quizzList = result.data)
                    updateLoading(false)
                }
                is Result.Error -> {
                    updateLoading(false)
                }
            }
        }
    }

    fun onEvent(event: QuizzListEvent) {
        when (event) {
            is QuizzListEvent.OnTabClick -> if (event.nextTab == 0) {
                getUserCreatedQuizzList()
            } else {
                getUserQuizzList()
            }
        }
    }

    // region update state

    @VisibleForTesting
    fun updateLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }
}