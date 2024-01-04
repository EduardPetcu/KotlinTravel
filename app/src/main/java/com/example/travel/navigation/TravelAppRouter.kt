package com.example.travel.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {

    object RegisterScreen : Screen()
    object LoginScreen : Screen()
    object HomeScreen : Screen()
}

object TravelAppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.RegisterScreen)

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
    }
}