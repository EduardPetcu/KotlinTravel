package com.example.travel.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.travel.data.login.LoginViewModel
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.UserProfile


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculateScreen(loginViewModel: LoginViewModel = viewModel()) {
    Log.d("CalculateScreen", "CalculateScreen")
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
            modifier = Modifier.fillMaxSize(),
        ){
            Scaffold(bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 1) },
                containerColor = Color.hsl(236f, 0.58f, 0.52f)) {
            }
        }
    }
    UserProfile()
}

@Preview
@Composable
fun CalculateScreenPreview() {
    CalculateScreen()
}