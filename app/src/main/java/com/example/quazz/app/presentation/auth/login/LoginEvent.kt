package com.example.quazz.app.presentation.auth.login

import com.example.quazz.navigation.Route

sealed interface LoginEvent {
    data class SignIn(val openAndPopUp: (Route, Route) -> Unit): LoginEvent
    data class SignUp(val openScreen: (Route) -> Unit): LoginEvent
    data class UpdateEmail(val email: String) : LoginEvent
    data class UpdatePassword(val password: String) : LoginEvent
    data object ClearSignInError: LoginEvent
    data object ClearFieldsError: LoginEvent
}