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
    navigation(startDestination = Route.LoginRoute.route, route = Route.AuthRoute.route){
        composable(route = Route.LoginRoute.route) {
            LoginScreen(
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                openScreen = { route -> appState.navigate(route) }
            )
        }
        composable(route = Route.RegisterRoute.route) {
            RegisterScreen(navigation = { appState.popUp() })
        }
    }
}

