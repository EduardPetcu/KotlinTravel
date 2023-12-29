package com.example.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.travel.app.TravelApp
import com.example.travel.screens.LoginScreen
import com.example.travel.screens.RegisterScreen
import com.example.travel.ui.theme.TravelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelApp()
        }
    }
}

//@Composable
//fun LoginScreen() {
//    val focusManager = LocalFocusManager.current
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Gray)
//            .padding(24.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.eggtransp),
//            contentDescription = "Logo",
//            Modifier.size(80.dp),
//        )
//        TextInput(InputType.Name, KeyboardActions(onNext = {
//            focusManager.moveFocus(FocusDirection.Down)
//        }))
//
//        TextInput(InputType.Password, KeyboardActions({}))
//
//        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
//            Text(text = stringResource(id = R.string.login), modifier = Modifier.padding(vertical = 8.dp))
//        }
//        Divider(
//            color = Color.White.copy(alpha = 0.3f),
//            thickness = 1.dp,
//            modifier = Modifier.padding(top = 48.dp)
//        )
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                text = stringResource(id = R.string.dont_have_account),
//                color = Color.White,
//                modifier = Modifier.padding(end = 8.dp)
//            )
//            TextButton(onClick = {}) {
//                Text(text = stringResource(id = R.string.sign_up))
//            }
//        }
//    }
//
//}

sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Name : InputType(
        label = "Username",
        icon = Icons.Default.Person,
        KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        label = "Password",
        icon = Icons.Default.Lock,
        KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )

    object Email : InputType(
        label = "Email",
        icon = Icons.Default.Email,
        KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
        visualTransformation = VisualTransformation.None
    )
}

//@Composable
//fun TextInput(
//    inputType: InputType,
//    KeyboardActions: KeyboardActions
//) {
//    var value by remember {
//        mutableStateOf("")
//    }
//
//    TextField(
//        value = value,
//        onValueChange = { value = it },
//        modifier = Modifier
//            .padding(horizontal = 16.dp)
//            .fillMaxWidth(),
//        leadingIcon = {
//            Icon(
//                imageVector = inputType.icon,
//                contentDescription = inputType.label,
//                modifier = Modifier.size(24.dp)
//            )
//        },
//        label = {
//            Text(text = inputType.label)
//        },
//        keyboardOptions = inputType.keyboardOptions,
//        visualTransformation = inputType.visualTransformation,
//        singleLine = true,
//        keyboardActions = KeyboardActions
//        )
//}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    TravelTheme {
        LoginScreen()
    }
}