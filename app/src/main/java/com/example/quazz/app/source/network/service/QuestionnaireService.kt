package com.example.quazz.app.source.network.service

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.Quizz

interface QuestionnaireService {
    suspend fun createQuestionnaire(
        quizz: Quizz
    ): Result<Unit, Error>
}