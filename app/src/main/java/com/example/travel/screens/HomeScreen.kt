package com.example.travel.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.components.CurrentLocationScreen
import com.example.travel.components.ProfileContent
import com.example.travel.components.SignOutButton
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(loginViewModel: LoginViewModel = viewModel()) {
    TravelTheme {
        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 0) },
        ) { padding ->
            ProfileContent(
                modifier = Modifier
                    .padding(padding)
            ) {
                Row() {
                    UserProfile()
                    Spacer(modifier = Modifier.padding(horizontal = 30.dp))
                    // position the icon on the top right corner of the screen
                    Icon(imageVector = Icons.AutoMirrored.Default.Logout, contentDescription = "Sign Out", modifier = Modifier.padding(vertical = 18.dp).clickable {
                        loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
                        TravelAppRouter.navigateTo(Screen.LoginScreen)
                    })
                }
                Column() {
                    CurrentLocationScreen()
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

