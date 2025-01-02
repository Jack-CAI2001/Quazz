package com.example.quazz.app.presentation.quizzList.quizz

import com.example.quazz.app.model.Quizz

data class QuizzState(
    val isLoading: Boolean = false,
    val quizz: Quizz = Quizz(),
    val isEdit: Boolean = false,
)