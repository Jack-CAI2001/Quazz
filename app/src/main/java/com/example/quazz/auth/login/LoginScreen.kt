package com.example.quazz.auth.login

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
import com.example.quazz.navigation.Route

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier){
    Column(modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text= "Login Screen", fontSize = 40.sp)
        Spacer(modifier.height(40.dp))
        Button(onClick = {
            navController.navigate(Route.AppRoute.route) {
                popUpTo(Route.AuthRoute.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }) {
            Text(text = "go to appHome")
        }
        Button(onClick = {
            navController.navigate(Route.RegisterRoute.route) {
                launchSingleTop = true
            }
        }
        ) {
            Text(text = "go to register")
        }
    }
}