package com.example.quazz.app.presentation.auth.register

import com.example.quazz.app.presentation.UiText

data class RegisterState(
    val email: String = "",
    val emailError: UiText = UiText.DynamicString(""),
    val pseudo: String = "",
    val pseudoError: UiText = UiText.DynamicString(""),
    val password: String = "",
    val passwordError: UiText = UiText.DynamicString(""),
    val confirmPassword: String = "",
    val confirmPasswordError: UiText = UiText.DynamicString(""),
    val isLoading: Boolean = false,
    val signUpError:  UiText = UiText.DynamicString("")
)