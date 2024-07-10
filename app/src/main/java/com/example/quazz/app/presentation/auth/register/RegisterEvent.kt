package com.example.quazz.app.presentation.auth.register

sealed interface RegisterEvent {
    data class UpdateEmail(val email: String) : RegisterEvent
    data class UpdatePassword(val password: String) : RegisterEvent
    data object ClearPasswordError: RegisterEvent
    data object ClearEmailError: RegisterEvent
    data object ClearConfirmPasswordError: RegisterEvent
    data class UpdateConfirmPassword(val confirmPassword: String) : RegisterEvent
    data object SignUp : RegisterEvent
    data object ClearSignUpError : RegisterEvent
}