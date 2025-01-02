package com.example.quazz.app.presentation.quizzList.createQuizz

import com.example.quazz.app.model.Questionnaire

data class CreateQuizzState(
    val author: String = "",
    val title: String = "",
    val description: String = "",
    val questionnaires: List<Questionnaire> = emptyList(),
    val errorStart: String = "",
    val errorList: List<String> = emptyList(),
    val currentIndex: Int = -1

)