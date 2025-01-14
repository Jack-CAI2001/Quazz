package com.example.quazz.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.repository.AuthRepository
import com.example.quazz.app.repository.QuestionnaireRepository
import com.example.quazz.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private var isRandomQuizzCalled = false

    init {
        getRandomQuizz()
    }

    private fun addQuizz(quizz: Quizz) {
        viewModelScope.launch {
            when(val result = userRepository.addQuizzToQuizzList(quizz, authRepository.currentUserUid)) {
                is Result.Success -> {

                }
                is Result.Error -> {

                }
            }
        }
    }

    private fun getRandomQuizz() {
        viewModelScope.launch {
            updateLoading(true)
            when(val result = questionnaireRepository.getRandomQuizz()) {
                is Result.Success -> {
                    addQuizzList(result.data)
                    isRandomQuizzCalled = false
                    updateLoading(false)
                }
                is Result.Error -> {
                    isRandomQuizzCalled = false
                    updateLoading(false)
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LastQuizz -> {
                if (_uiState.value.currentIndex == _uiState.value.quizzList.size -1 && !isRandomQuizzCalled) {
                    getRandomQuizz()
                    isRandomQuizzCalled = true
                }
            }

            is HomeEvent.AddQuizz -> {
                addQuizz(
                    _uiState.value.quizzList[_uiState.value.currentIndex]
                )
            }
        }
    }

    // region update state

    private fun addQuizzList(quizz: Quizz) {
        val tempQuizzList = _uiState.value.quizzList.toMutableList()
        tempQuizzList.add(quizz)
        _uiState.value = _uiState.value.copy(quizzList = tempQuizzList.toList())
    }

    private fun updateLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun updateCurrentIndex(currentIndex: Int) {
        _uiState.value = _uiState.value.copy(currentIndex = currentIndex)
    }
}