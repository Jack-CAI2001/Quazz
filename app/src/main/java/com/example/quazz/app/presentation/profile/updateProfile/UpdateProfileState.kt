package com.example.quazz.app.presentation.profile.updateProfile

import com.example.quazz.app.presentation.UiText

data class UpdateProfileState(
    val email: String = "",
    val newPassword: String = "",
    val currentPassword: String = "",
    val confirmPassword: String = "",
    val errorMessage: UiText = UiText.DynamicString(""),
)