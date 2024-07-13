package com.example.quazz.app.presentation.auth.login

sealed interface LoginEvent {
    data class SignIn(val openAndPopUp: (String, String) -> Unit): LoginEvent
    data class SignUp(val openScreen: (String) -> Unit): LoginEvent
    data class UpdateEmail(val email: String) : LoginEvent
    data class UpdatePassword(val password: String) : LoginEvent
    data object ClearSignInError: LoginEvent
    data object ClearFieldsError: LoginEvent
}