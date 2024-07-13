package com.example.quazz.app.presentation.auth.login

import com.example.quazz.app.presentation.UiText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val signinError: UiText = UiText.DynamicString(""),
    val fieldsError: UiText = UiText.DynamicString(""),
    val isLoading: Boolean = false
)