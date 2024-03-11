package com.example.travel.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import com.example.travel.components.ProfileContent
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.UserProfile


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculateScreen() {
    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 1) },
        containerColor = Color.hsl(236f, 0.58f, 0.52f)
    ) { padding ->
        ProfileContent(
            modifier = Modifier
                .padding(padding)
        ) {
            UserProfile()
        }
    }
}

@Preview
@Composable
fun CalculateScreenPreview() {
    CalculateScreen()
}