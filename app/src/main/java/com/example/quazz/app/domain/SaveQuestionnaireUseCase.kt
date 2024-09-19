package com.example.quazz.app.domain

import com.example.quazz.app.model.Questionnaire
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
                when (questionnaireRepository.createQuestionnaire(user.data, title, description, questionnaire)) {
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