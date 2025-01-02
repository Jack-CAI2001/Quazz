package com.example.quazz.app.presentation.quizzList.quizz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.repository.AuthRepository
import com.example.quazz.app.repository.QuestionnaireRepository
import com.example.quazz.app.repository.UserRepository
import com.example.quazz.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizzViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val questionnaireRepository: QuestionnaireRepository
) : ViewModel() {

    private val quizzRoute = savedStateHandle.toRoute<Route.QuizzRoute>()

    private val _uiState = MutableStateFlow(QuizzState())
    val uiState: StateFlow<QuizzState> = _uiState.asStateFlow()

    init {
        updateQuizz(Quizz(id = quizzRoute.quizzId))

        // 0 is from quizzCreated
        // 1 is from user Quizz
        when(quizzRoute.selectedTabIndex) {
            0 -> getQuizzById(quizzRoute.quizzId)
            1 -> getUserQuizzById(quizzRoute.quizzId)
        }
    }

    private fun getUserQuizzById(quizzId: String) {
        viewModelScope.launch {
            updateLoading(true)
            when(val result = userRepository.getUserQuizzById(quizzId, authRepository.currentUserUid)) {
                is Result.Error -> {updateLoading(false)}
                is Result.Success -> {
                    updateLoading(false)
                    updateQuizz(result.data)
                }
            }
        }
    }

    private fun getQuizzById(quizzId : String) {
        viewModelScope.launch {
            updateLoading(true)
            when(val result = questionnaireRepository.getQuizzById(quizzId)) {
                is Result.Error -> {updateLoading(false)}
                is Result.Success -> {
                    updateLoading(false)
                    updateQuizz(result.data)
                }
            }
        }
    }

    fun onEvent(event: QuizzEvent) {
        when (event) {
            is QuizzEvent.Delete -> TODO()
            is QuizzEvent.Edit -> updateIsEdit(event.isEditing)
            is QuizzEvent.Run -> TODO()
        }
    }

    // region update state

    private fun updateLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private fun updateQuizz(quizz: Quizz) {
        _uiState.value = _uiState.value.copy(quizz = quizz)
    }

    private fun updateIsEdit(isEdit: Boolean) {
        _uiState.value = _uiState.value.copy(isEdit = isEdit)
    }

}