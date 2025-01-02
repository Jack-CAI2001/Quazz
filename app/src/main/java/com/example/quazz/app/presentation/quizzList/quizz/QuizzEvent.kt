package com.example.quazz.app.presentation.quizzList.quizz

sealed interface QuizzEvent {
    data class Edit(val isEditing: Boolean): QuizzEvent
    data object Run: QuizzEvent
    data object Delete: QuizzEvent
}