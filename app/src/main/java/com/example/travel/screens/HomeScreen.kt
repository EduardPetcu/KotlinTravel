package com.example.travel.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.R
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView


@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomeScreen(loginViewModel: LoginViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.hsl(236f, 0.58f, 0.52f))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tabBarItems = listOf(TabBarItem.homeTab, TabBarItem.calculteTab, TabBarItem.transportTab, TabBarItem.profileTab)
        Button(onClick = {
            loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
            TravelAppRouter.navigateTo(Screen.LoginScreen)
            // Change color of the button background
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = stringResource(id = R.string.sign_out), modifier = Modifier.padding(vertical = 8.dp))
        }

        Scaffold(bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 0) },
            containerColor = Color.hsl(236f, 0.58f, 0.52f)) {
        }
    }
}
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}