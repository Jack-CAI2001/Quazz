package com.example.quazz.app.presentation.splash

import androidx.lifecycle.ViewModel
import com.example.quazz.app.domain.UserConnectedUseCase
import com.example.quazz.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userConnectedUseCase: UserConnectedUseCase,
): ViewModel() {
    fun onAppStart(openAndPopUp: (Route, Route) -> Unit) {
        if (userConnectedUseCase.invoke()) openAndPopUp(Route.AppRoute, Route.SplashRoute)
        else openAndPopUp(Route.LoginRoute, Route.SplashRoute)
    }
}