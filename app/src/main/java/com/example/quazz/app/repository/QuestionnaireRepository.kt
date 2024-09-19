package com.example.quazz.app.repository

import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.QuestionnaireService
import javax.inject.Inject

class QuestionnaireRepository @Inject constructor(
    private val questionnaireService: QuestionnaireService
) {
    suspend fun createQuestionnaire(
        user: User,
        title: String,
        description: String,
        questionnaire: List<Questionnaire>
    ) = questionnaireService.createQuestionnaire(user, title, description, questionnaire)
}