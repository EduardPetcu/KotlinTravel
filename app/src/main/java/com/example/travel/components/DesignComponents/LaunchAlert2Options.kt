package com.example.travel.components.DesignComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.travel.ui.theme.ConfirmGreen
import com.example.travel.ui.theme.ContainerYellow
import com.example.travel.ui.theme.DeclineRed
import com.example.travel.ui.theme.TravelTheme

@Composable
fun LaunchAlert(
    title: String,
    question: String,
    s1: String,
    s2: String,
    onConfirm: () -> Unit = { },
    onDismissRequest: () -> Unit = { }
) {
    TravelTheme {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .heightIn(max = 170.dp),
                color = ContainerYellow
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = title, style = MaterialTheme.typography.headlineMedium)
                    Text(text = question, style = MaterialTheme.typography.bodyLarge)
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Button(
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(containerColor = ConfirmGreen)
                        ) {
                            Text(text = s1)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.buttonColors(containerColor = DeclineRed)
                        ) {
                            Text(text = s2)
                        }
                    }
                }
            }
        }
    }

}