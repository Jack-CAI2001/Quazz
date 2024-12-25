package com.example.quazz.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.quazz.QuazzAppState
import com.example.quazz.app.presentation.auth.login.LoginScreen
import com.example.quazz.app.presentation.auth.register.RegisterScreen

fun NavGraphBuilder.authGraph(
    appState: QuazzAppState,
){
    navigation<Route.AuthRoute>(
        startDestination = Route.LoginRoute
    ) {
        composable<Route.LoginRoute> {
            LoginScreen(
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                openScreen = { route -> appState.navigate(route) }
            )
        }
        composable<Route.RegisterRoute> {
            RegisterScreen(navigation = { appState.popUp() })
        }
    }
}

