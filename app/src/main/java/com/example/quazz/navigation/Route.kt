package com.example.quazz.navigation

sealed class Route(val route: String) {
    data object AuthRoute : Route(route = "Auth")
    data object LoginRoute : Route(route = "Login")
    data object RegisterRoute : Route(route = "Register")
    data object AppRoute : Route(route = "App")
    data object HomeRoute : Route(route = "Home")
    data object SearchRoute : Route(route = "Search")
    data object ProfileRoute : Route(route = "Profile")
    data object CreateRoute : Route(route = "Create")
    data object SplashRoute: Route(route = "Splash")
}