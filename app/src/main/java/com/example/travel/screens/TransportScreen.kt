package com.example.travel.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travel.R
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.data.register.RegisterUIEvent
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.BottomNavItem
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransportScreen(loginViewModel: LoginViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.hsl(236f, 0.58f, 0.52f))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val navController = rememberNavController()
        val tabBarItems = listOf(TabBarItem.homeTab, TabBarItem.calculteTab, TabBarItem.transportTab, TabBarItem.profileTab)
        Surface (
            modifier = Modifier.fillMaxSize()
        ){
            Scaffold(bottomBar = { TabView(tabBarItems = tabBarItems) }) {
                NavHost(navController = navController, startDestination = "transport") {
                    composable("home") { Text(TabBarItem.homeTab.title) }
                    composable("calculate") { Text(TabBarItem.calculteTab.title) }
                    composable("transport") { Text(TabBarItem.transportTab.title) }
                    composable("profile") { Text(TabBarItem.profileTab.title) }
                }
            }
        }
        Button(onClick = {
           loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
           TravelAppRouter.navigateTo(Screen.LoginScreen)
            // Change color of the button background
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = stringResource(id = R.string.sign_out), modifier = Modifier.padding(vertical = 8.dp))
        }

    }
}

@Preview
@Composable
fun TransportScreenPreview() {
    TransportScreen()
}