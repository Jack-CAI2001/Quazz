package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.Questionnaire
import javax.inject.Inject

class QuestionnaireValidator @Inject constructor() {
    fun execute(questionnaire: Questionnaire): Result<Unit, Error> {
        when(questionnaire) {
            is Questionnaire.ChoiceQuestion -> {
                if (questionnaire.question.isBlank() || questionnaire.options.isEmpty() || questionnaire.options.any { it.isBlank() } || questionnaire.answer.none { it }) {
                    return Result.Error(Error.CommonError.EMPTY_FIELD)
                }
            }

            is Questionnaire.TextEntryQuestion -> {
                if (questionnaire.question.isBlank() || questionnaire.answer.isBlank()) {
                    return Result.Error(Error.CommonError.EMPTY_FIELD)
                }
            }
        }
        return Result.Success(Unit)

    }
}