package com.example.quazz.app.presentation.quizzList.runQuizz

sealed interface RunQuizzEvent {
    data class UpdateStringItem(val response: String, val indexQuestion: Int): RunQuizzEvent
    data class UpdateBooleanList(val indexOption: Int, val indexQuestion: Int): RunQuizzEvent
    data object OnComplete: RunQuizzEvent
}