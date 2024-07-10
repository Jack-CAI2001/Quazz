package com.example.quazz.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.quazz.app.presentation.auth.login.LoginScreen
import com.example.quazz.app.presentation.auth.register.RegisterScreen

fun NavGraphBuilder.authGraph(navController: NavController){
    navigation(startDestination = Route.LoginRoute.route, route = Route.AuthRoute.route){
        composable(route = Route.LoginRoute.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Route.RegisterRoute.route) {
            RegisterScreen(navigation = { navController.navigateUp() })
        }
    }
}

