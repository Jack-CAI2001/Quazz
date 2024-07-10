package com.example.quazz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quazz.navigation.Nav
import com.example.quazz.ui.theme.AppTheme
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureFirebaseServices()
        setContent {
            AppTheme {
                Nav()
            }
        }
    }
    private fun configureFirebaseServices() {
        if (BuildConfig.DEBUG) {
            Firebase.auth.useEmulator(LOCALHOST, AUTH_PORT)
        }
    }
}

const val LOCALHOST = "10.0.2.2"
const val AUTH_PORT = 9099
const val FIRESTORE_PORT = 8080