package com.example.travel.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.travel.data.travel.TravelViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.screens.RegisterScreen
import com.example.travel.screens.LoginScreen
import com.example.travel.screens.HomeScreen

@Composable
fun TravelApp(travelViewModel: TravelViewModel = TravelViewModel()) {
    travelViewModel.checkForActiveSession()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        if (travelViewModel.isUserLoggedIn.value == true) {
            TravelAppRouter.navigateTo(Screen.HomeScreen)
        }
        when (TravelAppRouter.currentScreen.value) {
            Screen.RegisterScreen -> RegisterScreen()
            Screen.LoginScreen -> LoginScreen()
            Screen.HomeScreen -> HomeScreen()
        }
    }

}