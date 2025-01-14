package com.example.quazz.app.presentation.home

sealed interface HomeEvent {
    data object LastQuizz: HomeEvent
    data object AddQuizz: HomeEvent
}