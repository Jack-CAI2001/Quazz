package com.example.quazz.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@SuppressLint("RestrictedApi")
@Composable
fun Nav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.AuthRoute.route) {
        authGraph(navController)
        appGraph(navController)
    }

    // to suppress
    navController.addOnDestinationChangedListener { controller, _, _ ->
        val routes = controller
            .currentBackStack.value
            .map { it.destination.route }
            .joinToString(", ")
        Log.d("BackStackLog", "BackStack: $routes")
    }
}