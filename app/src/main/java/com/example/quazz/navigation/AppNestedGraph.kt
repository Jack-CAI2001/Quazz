package com.example.quazz.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.quazz.QuazzAppState
import com.example.quazz.app.presentation.create.CreateScreen
import com.example.quazz.app.presentation.home.HomeScreen
import com.example.quazz.app.presentation.profile.ProfileScreen
import com.example.quazz.app.presentation.search.SearchScreen
import com.example.quazz.core.components.ScaffoldBottomApp

fun NavGraphBuilder.appGraph(appState: QuazzAppState){
    navigation(startDestination = Route.HomeRoute.route, route = Route.AppRoute.route){
        composable(route = Route.HomeRoute.route) {
            ScaffoldBottomApp(navController = appState.navController) {
                HomeScreen(it, appState.navController)
            }
        }
        composable(route = Route.SearchRoute.route) {
            ScaffoldBottomApp(navController = appState.navController) {
                SearchScreen(it, appState.navController)
            }
        }
        composable(route = Route.ProfileRoute.route) {
            ScaffoldBottomApp(navController = appState.navController) {
                ProfileScreen(
                    paddingValues = it,
                    restartApp = { route -> appState.clearAndNavigate(route) })
            }
        }
        composable(route = Route.CreateRoute.route) {
            ScaffoldBottomApp(navController = appState.navController) {
                CreateScreen(it, appState.navController)
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(Route.HomeRoute.route, Icons.Default.Home, "Home")
    data object Search : BottomNavItem(Route.SearchRoute.route, Icons.Default.Search, "Search")
    data object Profile : BottomNavItem(Route.ProfileRoute.route, Icons.Default.Person, "Profile")
    data object Create : BottomNavItem(Route.CreateRoute.route, Icons.Default.AddCircle, "Create")

}