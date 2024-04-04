package com.example.travel.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import com.example.travel.components.ProfileComponents.AchievementsLayout
import com.example.travel.components.ProfileComponents.DescriptionText
import com.example.travel.components.ProfileComponents.TopProfileLayout
import com.example.travel.components.ProfileContent
import com.example.travel.data.User
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.fetchUserData

val tabBarItems = listOf(TabBarItem.homeTab, TabBarItem.calculteTab, TabBarItem.transportTab, TabBarItem.profileTab)
@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen() {
    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 3) },
        containerColor = Color.hsl(236f, 0.58f, 0.52f)
    ) {
        padding -> ProfileContent(
            modifier = Modifier
                .padding(padding)
        ) {
            var userInfo by remember { mutableStateOf<User?>(null) }
            val context = LocalContext.current
            LaunchedEffect(key1 = true) {
                fetchUserData { user ->
                    userInfo = user
                }
            }
            TopProfileLayout(userInfo, context)
            DescriptionText(userInfo, context)
            AchievementsLayout(userInfo)

        }


    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
