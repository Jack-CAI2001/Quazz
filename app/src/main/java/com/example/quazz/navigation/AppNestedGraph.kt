package com.example.quazz.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.quazz.QuazzAppState
import com.example.quazz.app.presentation.home.HomeScreen
import com.example.quazz.app.presentation.profile.ProfileScreen
import com.example.quazz.app.presentation.quizzList.QuizzListScreen
import com.example.quazz.app.presentation.quizzList.createQuizz.CreateQuizzScreen
import com.example.quazz.app.presentation.quizzList.quizz.QuizzScreen
import com.example.quazz.app.presentation.search.SearchScreen
import com.example.quazz.core.components.BaselineQuiz24
import com.example.quazz.core.components.ProvideAnimatedVisibilityScope
import com.example.quazz.core.components.ScaffoldBottomApp

fun NavGraphBuilder.appGraph(appState: QuazzAppState){
    navigation<Route.AppRoute>(startDestination = Route.HomeRoute) {
        composable<Route.HomeRoute> {
            ScaffoldBottomApp(navController = appState.navController) {
                HomeScreen(it, appState.navController)
            }
        }
        composable<Route.SearchRoute> {
            ScaffoldBottomApp(navController = appState.navController) {
                SearchScreen(it, appState.navController)
            }
        }
        composable<Route.ProfileRoute> {
            ScaffoldBottomApp(navController = appState.navController) {
                ProfileScreen(
                    paddingValues = it,
                    restartApp = { route -> appState.clearAndNavigate(route) })
            }
        }
        composable<Route.CreateListRoute> {
            ProvideAnimatedVisibilityScope {
                ScaffoldBottomApp(navController = appState.navController) {
                    QuizzListScreen(it, appState.navController)
                }
            }
        }
        composable<Route.CreateRoute> {
            ProvideAnimatedVisibilityScope {
                CreateQuizzScreen(
                    popUp = { appState.popUp() }
                )
            }
        }
        composable<Route.QuizzRoute> {
            QuizzScreen({ appState.popUp() })
        }
    }
}

sealed class BottomNavItem(val route: Route, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(Route.HomeRoute, Icons.Default.Home, "Home")
    data object Search : BottomNavItem(Route.SearchRoute, Icons.Default.Search, "Search")
    data object Profile : BottomNavItem(Route.ProfileRoute, Icons.Default.Person, "Profile")
    data object Quizz : BottomNavItem(Route.CreateListRoute, BaselineQuiz24, "Quizz")

}