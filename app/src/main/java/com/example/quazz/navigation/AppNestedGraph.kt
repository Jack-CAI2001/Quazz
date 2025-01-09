package com.example.quazz.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.quazz.QuazzAppState
import com.example.quazz.app.model.CustomNavType
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.presentation.home.HomeScreen
import com.example.quazz.app.presentation.profile.ProfileScreen
import com.example.quazz.app.presentation.quizzList.QuizzListScreen
import com.example.quazz.app.presentation.quizzList.createQuizz.CreateQuizzScreen
import com.example.quazz.app.presentation.quizzList.quizz.QuizzScreen
import com.example.quazz.app.presentation.quizzList.runQuizz.RunQuizzScreen
import com.example.quazz.app.presentation.search.SearchScreen
import com.example.quazz.core.components.BaselineQuiz24
import com.example.quazz.core.components.ProvideAnimatedVisibilityScope
import com.example.quazz.core.components.ScaffoldBottomApp
import kotlin.reflect.typeOf

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
            QuizzScreen({ appState.popUp() }, { quizz -> appState.navController.navigate(Route.RunQuizzRoute(quizz))})
        }
        composable<Route.RunQuizzRoute>(
            typeMap = mapOf(
                typeOf<Quizz>() to CustomNavType.QuizzType
            ),
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(300))
            }
        ) {
            RunQuizzScreen({ appState.popUp() })
        }
    }
}

sealed class BottomNavItem(val route: Route, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(Route.HomeRoute, Icons.Default.Home, "Home")
    data object Search : BottomNavItem(Route.SearchRoute, Icons.Default.Search, "Search")
    data object Profile : BottomNavItem(Route.ProfileRoute, Icons.Default.Person, "Profile")
    data object Quizz : BottomNavItem(Route.CreateListRoute, BaselineQuiz24, "Quizz")

}