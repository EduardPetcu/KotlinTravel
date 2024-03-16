package com.example.travel.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.components.ProfileContent
import com.example.travel.components.SelectIntervalDate
import com.example.travel.components.SignOutButton
import com.example.travel.data.login.LoginViewModel
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile

// TODO: Change the SignOut button into an icon that should appear on top right corner of the screen
// TODO: Hardcode some travel options for users to select from
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
                UserProfile()
                Row() {
                    SignOutButton(loginViewModel = loginViewModel)
                    // SelectIntervalDate("Start Date")
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

