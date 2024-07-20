package com.example.quazz.app.presentation.profile.updateProfile

sealed interface UpdateProfileEvent {
    data class Save(val model: UpdateProfile, val onSaveClick: () -> Unit) : UpdateProfileEvent
    data class UpdateEmail(val email: String) : UpdateProfileEvent
    data class UpdateNewPassword(val newPassword: String) : UpdateProfileEvent
    data class UpdateCurrentPassword(val currentPassword: String) : UpdateProfileEvent
    data class UpdateConfirmPassword(val confirmPassword: String) : UpdateProfileEvent
    data object ClearErrorMessage : UpdateProfileEvent

}