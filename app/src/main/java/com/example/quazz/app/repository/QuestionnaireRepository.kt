package com.example.quazz.app.repository

import com.example.quazz.app.model.Quizz
import com.example.quazz.app.source.network.service.QuestionnaireService
import javax.inject.Inject

class QuestionnaireRepository @Inject constructor(
    private val questionnaireService: QuestionnaireService
) {
    suspend fun createQuestionnaire(
        quizz: Quizz
    ) = questionnaireService.createQuestionnaire(quizz)

    suspend fun getQuizzById(quizzId : String) = questionnaireService.getQuizzById(quizzId)

    suspend fun getRandomQuizz() = questionnaireService.getRandomQuizz()
}