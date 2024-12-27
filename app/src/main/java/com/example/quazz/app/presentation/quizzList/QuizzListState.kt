package com.example.quazz.app.presentation.quizzList

import com.example.quazz.app.model.Quizz

data class QuizzListState(
    val isLoading: Boolean = false,
    val quizzList: List<Quizz> = emptyList()
)