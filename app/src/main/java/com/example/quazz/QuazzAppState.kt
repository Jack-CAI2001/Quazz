package com.example.quazz

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.example.quazz.navigation.Route

@Stable
class QuazzAppState(val navController: NavHostController) {
    fun popUp() {
        navController.navigateUp()
    }

    fun navigate(route: Route) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: Route, popUp: Route) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }
    fun clearAndNavigate(route: Route) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}