package com.example.quazz.app.presentation.profile

sealed interface ProfileEvent {
    data object LogOut : ProfileEvent
    data class UpdatePseudo(val pseudo: String): ProfileEvent
    data object SavePseudo : ProfileEvent
    data object ClearError: ProfileEvent
}