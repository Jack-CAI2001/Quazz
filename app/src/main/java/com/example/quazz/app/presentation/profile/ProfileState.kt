package com.example.quazz.app.presentation.profile

import com.example.quazz.app.model.User
import com.example.quazz.app.presentation.UiText

data class ProfileState(
    val isUserConnected: Boolean = false,
    val pseudoText: String = "",
    val user: User = User(),
    val isLoading: Boolean = false,
    val errorMessage: UiText = UiText.DynamicString(""),
)