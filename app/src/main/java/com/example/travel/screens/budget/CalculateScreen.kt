package com.example.travel.screens.budget

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel.components.BudgetComponents.BudgetList
import com.example.travel.components.ProfileContent
import com.example.travel.data.Budget
import com.example.travel.data.User
import com.example.travel.repository.BudgetRepository
import com.example.travel.repository.BudgetRepositoryImpl
import com.example.travel.screens.tabBarItems
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.ContainerYellow
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile
import com.example.travel.ui.theme.fetchUserInfo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculateScreen() {
    TravelTheme {
        val budgetRepository: BudgetRepository = BudgetRepositoryImpl()
        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 1) },
            containerColor = BackgroundBlue
        ) { padding ->
            ProfileContent(
                modifier = Modifier
                    .padding(padding)
            ) {
                val userAuth = FirebaseAuth.getInstance().currentUser?.uid
                var userInfo by remember { mutableStateOf<User?>(null) }
                var listBudgets by remember { mutableStateOf<List<Budget>?>(emptyList()) }
                val budgetGen = BudgetList()
                LaunchedEffect(key1 = true) {
                    val userDeferred = async { fetchUserInfo() }
                    delay(2000)
                    val budgetDeferred =
                        async { budgetRepository.getBudgetsFromUserName(userAuth!!) }
                    userInfo = userDeferred.await()
                    Log.d("CalculateScreen", "userInfo: $userInfo")
                    listBudgets = budgetDeferred.await()
                }
                Column() {
                    UserProfile()
                    if (userInfo == null || listBudgets == null) {
                        LinearProgressIndicator(
                            color = ContainerYellow,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    } else {
                        budgetGen.BudgetListGenerator(listBudgets!!)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CalculateScreenPreview() {
    CalculateScreen()
}