package com.example.quazz.app.presentation.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateListState())
    val uiState: StateFlow<CreateListState> = _uiState.asStateFlow()

    init {

        // get CreatedList by user
        // QuizzCreated list docRef de Quizz
        // Quizz author title et question
        // question has option answer and question
    }

    private fun getUserCreatedQuizzList() {
        // get user created quizz list
    }

    fun onEvent(event: CreateListEvent) {
        when (event) {

            else -> {}
        }
    }
}