package com.example.quazz

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class QuazzAppState(val navController: NavHostController) {
    fun popUp() {
        navController.navigateUp()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }
}