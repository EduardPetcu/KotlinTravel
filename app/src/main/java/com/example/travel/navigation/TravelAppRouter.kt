package com.example.travel.navigation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth

sealed class Screen {

    object RegisterScreen : Screen() {
        const val route = "register"
    }
    object LoginScreen : Screen() {
        const val route = "login"
    }
    object HomeScreen : Screen() {
        const val route = "home"
    }
    object CalculateScreen : Screen() {
        const val route = "calculate"
    }
    object TransportScreen : Screen() {
        const val route = "transport"
    }
    object ProfileScreen : Screen() {
        const val route = "profile"
    }
}

object TravelAppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.RegisterScreen)
    fun navigateTo(destination: Screen) {
        Log.d("TravelAppRouter", "navigateTo: $destination")
        currentScreen.value = destination
    }
}