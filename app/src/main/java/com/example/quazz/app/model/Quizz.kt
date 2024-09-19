package com.example.quazz.app.model

data class Quizz(
    val author: String = "",
    val title: String = "",
    val questionnaires: List<Questionnaire> = emptyList()
)