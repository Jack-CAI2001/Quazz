package com.example.quazz.app.presentation.home

import com.example.quazz.app.model.Quizz

data class HomeState(
    val quizzList : List<Quizz> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false
)