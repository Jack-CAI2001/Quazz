package com.example.quazz.core.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quazz.navigation.BottomNavItem
import com.example.quazz.navigation.Route
import com.example.quazz.ui.theme.AppTheme

@Composable
fun BottomNavigationBar(navController: NavController, screens:  List<BottomNavItem>, startDestination: String) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        screens.forEach { screen ->
            AddScreen(screen = screen, selected = currentRoute == screen.route) {
                navController.navigate(screen.route)
                {
                    popUpTo(startDestination)
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun RowScope.AddScreen(screen: BottomNavItem, selected: Boolean, onClick: () -> Unit) {
    NavigationBarItem(
        selected = selected,
        label = { Text(text = screen.label) },
        icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
        onClick = { onClick() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldBottomApp(navController: NavController, content: @Composable (PaddingValues) -> Unit) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Create,
        BottomNavItem.Profile,
        )
    val startDestination = Route.HomeRoute.route
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, screens, startDestination)
        }
    ) {
        content(it)
    }
}

@PreviewLightDark
@Composable
fun ScaffoldBottomAppPreview() {
    AppTheme {
        ScaffoldBottomApp(navController = rememberNavController()) {
        }
    }
}