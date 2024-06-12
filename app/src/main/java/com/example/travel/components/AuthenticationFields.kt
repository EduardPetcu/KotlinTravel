package com.example.travel.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travel.ui.theme.InputType

@Composable
fun TextInput(
    inputType: InputType,
    KeyboardActions: KeyboardActions,
    onTextChanged: (String) -> Unit = {},
    errorStatus: Boolean = false
) {
    var textValue by rememberSaveable {
        mutableStateOf("")
    }

    TextField(
        value = textValue,
        onValueChange = { textValue = it
            onTextChanged(it)
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        leadingIcon = {
            inputType.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = inputType.label,
                    modifier = Modifier.size(24.dp)
                )
            }
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