package com.example.quazz.app.presentation.create.quizz

import com.example.quazz.app.model.Questionnaire

sealed interface CreateQuizzEvent {
    data object onNext: CreateQuizzEvent
    data object onPrevious: CreateQuizzEvent
    data object Save: CreateQuizzEvent
    data object onDelete: CreateQuizzEvent
    data class onQuestionTypeSelected(val type: Questionnaire): CreateQuizzEvent
    data class UpdateAuthor(val author: String): CreateQuizzEvent // delete ?
    data class UpdateTitle(val title: String): CreateQuizzEvent
    data class UpdateDescription(val description: String): CreateQuizzEvent
    data class UpdateQuestion(val question: String): CreateQuizzEvent
    data class AddOption(val option: String): CreateQuizzEvent
    data class DeleteOption(val index: Int): CreateQuizzEvent
    data class UpdateOption(val option: String, val index: Int): CreateQuizzEvent
    sealed class ChoiceQuestionEvent: CreateQuizzEvent {
        data class UpdateAnswer(val optionIndex: Int): CreateQuizzEvent
    }
    sealed class TextEntryQuestionEvent: CreateQuizzEvent {
        data class UpdateAnswer(val answer: String): CreateQuizzEvent
    }

}