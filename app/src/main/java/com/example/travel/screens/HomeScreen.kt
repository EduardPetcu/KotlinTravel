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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.components.CurrentLocationScreen
import com.example.travel.components.DesignComponents.LaunchAlert
import com.example.travel.components.ProfileContent
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
    var showSignOutDialog by remember { mutableStateOf(false) }
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
                    IconButton(onClick = {
                          showSignOutDialog = true },
                        modifier = Modifier.padding(vertical = 18.dp)) {
                        Icon(imageVector = Icons.AutoMirrored.Default.Logout, contentDescription = "Sign Out", tint = Color.White)
                    }
                }
                Column() {
                    CurrentLocationScreen()
                    if (showSignOutDialog) {
                         LaunchAlert("Sign Out", "Are you sure you want to sign out?", "Sign Out", "Cancel", onConfirm = {
                             loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
                             TravelAppRouter.navigateTo(Screen.LoginScreen)
                         }, onDismissRequest = { showSignOutDialog = false })
                    }
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

