package com.example.quazz.app.source.network.service

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.User

interface QuestionnaireService {
    suspend fun createQuestionnaire(
        user: User,
        title: String,
        description: String,
        questionnaire: List<Questionnaire>
    ): Result<Unit, DataError.Network>
}