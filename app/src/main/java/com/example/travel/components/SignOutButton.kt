package com.example.travel.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.TravelTheme

@Composable
fun SignOutButton(loginViewModel: LoginViewModel) {
    TravelTheme {
        Button(onClick = {
            loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
            TravelAppRouter.navigateTo(Screen.LoginScreen)
            // Change color of the button background
        }) {
            Text(
                text = stringResource(id = R.string.sign_out),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}