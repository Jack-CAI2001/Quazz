package com.example.quazz.app.presentation.quizzList

sealed interface QuizzListEvent {
    data class OnTabClick(val nextTab: Int): QuizzListEvent
}