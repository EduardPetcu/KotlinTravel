package com.example.travel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.R
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.data.register.RegisterUIEvent
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter

@Composable
fun HomeScreen(loginViewModel: LoginViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.eggtransp),
            contentDescription = "Logo",
            Modifier.size(80.dp),
        )
        Button(onClick = {
           loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
           TravelAppRouter.navigateTo(Screen.LoginScreen)
        }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.sign_out), modifier = Modifier.padding(vertical = 8.dp))
        }

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}