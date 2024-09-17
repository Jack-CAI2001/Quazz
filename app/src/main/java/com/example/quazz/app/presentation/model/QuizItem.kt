package com.example.quazz.app.presentation.model

sealed class QuizItem {
    data class MultipleChoiceQuestion(
        val question: String ,
        val options: List<String>,
        val correctAnswer: List<String>
    ) : QuizItem()

    data class TextEntryQuestion(
        val question: String,
        val answer: String
    ) : QuizItem()
}
