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
    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (userConnectedUseCase.invoke()) openAndPopUp(Route.AppRoute.route, Route.SplashRoute.route)
        else openAndPopUp(Route.LoginRoute.route, Route.SplashRoute.route)
    }
}