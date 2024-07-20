package com.example.quazz.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quazz.QuazzAppState
import com.example.quazz.app.presentation.splash.SplashScreen

@SuppressLint("RestrictedApi")
@Composable
fun Nav() {
    val appState = rememberAppState()
    NavHost(navController = appState.navController, startDestination = Route.SplashRoute.route) {
        composable(Route.SplashRoute.route) {
            SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
        }
        authGraph(appState)
        appGraph(appState)
    }

    // to suppress
    appState.navController.addOnDestinationChangedListener { controller, _, _ ->
        val routes = controller
            .currentBackStack.value
            .map { it.destination.route }
            .joinToString(", ")
        Log.d("BackStackLog", "BackStack: $routes")
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        QuazzAppState(navController)
    }