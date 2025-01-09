package com.example.quazz.navigation

import com.example.quazz.app.model.Quizz
import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object AuthRoute : Route()
    @Serializable
    data object LoginRoute : Route()
    @Serializable
    data object RegisterRoute : Route()
    @Serializable
    data object AppRoute : Route()
    @Serializable
    data object HomeRoute : Route()
    @Serializable
    data object SearchRoute : Route()
    @Serializable
    data object ProfileRoute : Route()
    @Serializable
    data object CreateListRoute : Route()
    @Serializable
    data object SplashRoute: Route()
    @Serializable
    data object CreateRoute : Route()
    @Serializable
    data class QuizzRoute(val quizzId: String, val selectedTabIndex: Int) : Route()
    @Serializable
    data class RunQuizzRoute(val quizz: Quizz): Route()
}