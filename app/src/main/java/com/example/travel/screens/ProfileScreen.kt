package com.example.travel.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import com.example.travel.data.Achievement
import com.example.travel.data.User
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.fetchUserInfo
import kotlinx.coroutines.async

val tabBarItems = listOf(TabBarItem.homeTab, TabBarItem.calculteTab, TabBarItem.mapTab, TabBarItem.profileTab)
@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
// TODO: Fix the bug where the UI does not update after new achievements received
fun ProfileScreen(user: User? = null) {
    var updatedAchievements by remember { mutableStateOf(false) }
    BackHandler (
        onBack = {
            TravelAppRouter.navigateTo(Screen.HomeScreen)
        })
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
            if (user == null) {
                LaunchedEffect(key1 = true) {
                    val userDeferred = async { fetchUserInfo() }
                    userInfo = userDeferred.await()
                    async {
                        updateAchievements(userInfo!!)
                        updatedAchievements = true
                    }.await()
                }
            } else {
                userInfo = user
                updatedAchievements = true
            }
            if (user == null) {
                TopProfileLayout(userInfo, context, true)
                DescriptionText(userInfo, context, true)
            } else {
                TopProfileLayout(userInfo, context, false)
                DescriptionText(userInfo, context, false)
            }
            if (updatedAchievements) {
                AchievementsLayout(userInfo)
            }
        }


    }
}

// TODO: Make a Dialog composable that displays for every new Achievement
fun updateAchievements(userInfo: User) {
    val userAchievements = userInfo.achievements.toMutableList()
    val allAchievements = Achievement.achievements.map { achievement -> achievement.title }

    if (userInfo.visitedCities.size >= 5 && !userAchievements.contains(allAchievements[1])) {
        userAchievements += allAchievements[1] // visited 5 cities
    }
    if (userInfo.budgets.isNotEmpty() && !userAchievements.contains(allAchievements[2])) {
        userAchievements += allAchievements[2] // planned a budget
    }
    if (userInfo.visitedCities.size >= 10 && !userAchievements.contains(allAchievements[3])) {
        userAchievements += allAchievements[3] // visited 10 cities
    }

    val updatedUser = userInfo.copy(achievements = userAchievements)
    val userDatabase = DatabaseRepositoryImpl()
    userDatabase.updateUserData(updatedUser)

}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
