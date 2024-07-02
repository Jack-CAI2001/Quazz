package com.example.quazz.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.quazz.auth.login.LoginScreen
import com.example.quazz.auth.register.RegisterScreen

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

