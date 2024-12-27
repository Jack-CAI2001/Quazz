package com.example.quazz.app.domain

import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.repository.QuestionnaireRepository
import javax.inject.Inject

class SaveQuestionnaireUseCase @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository,
    private val userUseCase: GetUserUseCase
)
{
    suspend operator fun invoke(title: String, description: String, questionnaire: List<Questionnaire>): Result<Unit, DataError.Network> {
        when (val user = userUseCase.invoke()) {
            is Result.Success -> {
                val quizz = Quizz(user = user.data, title = title, description = description, questionnaires = questionnaire)
                when (questionnaireRepository.createQuestionnaire(quizz)) {
                    is Result.Success -> {
                        return Result.Success(Unit)
                    }
                    is Result.Error -> {
                        return Result.Error(DataError.Network.UNKNOWN)
                    }
                }
            }
            is Result.Error -> {
                return Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }
}