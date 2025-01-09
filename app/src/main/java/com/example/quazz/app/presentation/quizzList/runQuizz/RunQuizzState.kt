package com.example.quazz.app.presentation.quizzList.runQuizz

import com.example.quazz.app.model.Quizz
import com.example.quazz.app.model.Response

data class RunQuizzState(
    val quizz: Quizz = Quizz(),
    val responses: List<Response> = emptyList(),
    val result: List<Int> = emptyList()
)