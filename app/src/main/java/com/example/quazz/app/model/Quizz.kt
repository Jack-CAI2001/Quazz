package com.example.quazz.app.model

data class Quizz(
    val id: String = "",
    val user: User = User(),
    val title: String = "",
    val description: String = "",
    val questionnaires: List<Questionnaire> = emptyList()
)