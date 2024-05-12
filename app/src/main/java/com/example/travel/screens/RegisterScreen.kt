package com.example.travel.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.data.register.RegisterUIEvent
import com.example.travel.data.register.RegisterViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.TravelTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.components.TextInput
import com.example.travel.ui.theme.InputType
import com.example.travel.components.DesignComponents.CarouselSlider
import com.example.travel.ui.theme.BackgroundBlue

// TODO: Add email verification
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = viewModel()) {
    TravelTheme {
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundBlue)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val images = listOf(
                R.drawable.goaindia,
                R.drawable.hanoivietnam,
                R.drawable.pelesromania
            )
            CarouselSlider(images)
            TextInput(InputType.Email, KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }), onTextChanged = {
                registerViewModel.onEvent(RegisterUIEvent.EmailChanged(it))
            }, errorStatus = registerViewModel.registrationUIState.value.isEmailValid)

            TextInput(InputType.Name, KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }), onTextChanged = {
                registerViewModel.onEvent(RegisterUIEvent.UsernameChanged(it))
            }, errorStatus = registerViewModel.registrationUIState.value.isUsernameValid)

            TextInput(InputType.Password, KeyboardActions(onDone = {
                registerViewModel.onEvent(RegisterUIEvent.RegisterClicked)
            }), onTextChanged = {
                registerViewModel.onEvent(RegisterUIEvent.PasswordChanged(it))
            }, errorStatus = registerViewModel.registrationUIState.value.isPasswordValid)

            Button(
                onClick = {
                    registerViewModel.onEvent(RegisterUIEvent.RegisterClicked)
                }, modifier = Modifier.fillMaxWidth(),
                enabled = registerViewModel.allValidationsPassed.value
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.have_acc),
                    color = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                TextButton(onClick = {
                    TravelAppRouter.navigateTo(Screen.LoginScreen)
                }) {
                    Text(
                        text = stringResource(id = R.string.action_sign_in_short),
                        color = Color.Yellow
                    )
                }
            }
            if (registerViewModel.signUpInProgress.value) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview()
@Composable
fun RegisterPreview() {
    RegisterScreen()
}