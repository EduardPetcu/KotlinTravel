package com.example.travel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.InputType
import com.example.travel.R
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.data.register.RegisterViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.TravelTheme

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.hsl(236f, 0.58f, 0.52f))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.travellogo2),
            contentDescription = "Logo",
            Modifier.size(300.dp),
        )
        TextInput(InputType.Email, KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }), onTextChanged = {
            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
        }, errorStatus = !loginViewModel.loginUIState.value.isEmailValid)

        TextInput(InputType.Password, KeyboardActions({}), onTextChanged = {
            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
        }, errorStatus = !loginViewModel.loginUIState.value.isPasswordValid)

        Button(onClick = {
            loginViewModel.onEvent(LoginUIEvent.LoginClicked)
        }, modifier = Modifier.fillMaxWidth(),
            enabled = loginViewModel.allValidationsPassed.value
        ) {
            Text(text = stringResource(id = R.string.login), modifier = Modifier.padding(vertical = 8.dp))
        }
        Divider(
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 48.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.dont_have_account),
                color = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            TextButton(onClick = {
                TravelAppRouter.navigateTo(Screen.RegisterScreen)
            }) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                    color = Color.Yellow
                )
            }
        }

        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun TextInput(
    inputType: InputType,
    KeyboardActions: KeyboardActions,
    onTextChanged: (String) -> Unit = {},
    errorStatus: Boolean = false
) {
    val textValue = remember {
        mutableStateOf("")
    }

    TextField(
        value = textValue.value,
        onValueChange = { textValue.value = it
                        onTextChanged(it)
                        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = inputType.icon,
                contentDescription = inputType.label,
                modifier = Modifier.size(24.dp)
            )
        },
        label = {
            Text(text = inputType.label)
        },
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        singleLine = true,
        keyboardActions = KeyboardActions,
        isError = !errorStatus
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    TravelTheme {
        LoginScreen()
    }
}