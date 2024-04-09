package com.example.travel.screens.budget

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.travel.components.BudgetComponents.BudgetList
import com.example.travel.components.ProfileContent
import com.example.travel.data.Budget
import com.example.travel.data.User
import com.example.travel.repository.BudgetRepository
import com.example.travel.repository.BudgetRepositoryImpl
import com.example.travel.screens.tabBarItems
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.UserProfile
import com.example.travel.ui.theme.fetchUserInfo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculateScreen() {
    val budgetRepository : BudgetRepository = BudgetRepositoryImpl()
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
            val userAuth = FirebaseAuth.getInstance().currentUser?.uid
            var userInfo by remember { mutableStateOf<User?>(null) }
            var listBudgets by remember { mutableStateOf<List<Budget>?>(emptyList()) }

            val context = LocalContext.current
            LaunchedEffect(key1 = true) {
                val userDeferred = async { fetchUserInfo() }
                val budgetDeferred = async { budgetRepository.getBudgetsFromUserName(userAuth!!) }
                userInfo = userDeferred.await()
                Log.d("CalculateScreen", "userInfo: $userInfo")
                listBudgets = budgetDeferred.await()
            }
            Column() {
                UserProfile()
                if (userInfo == null || listBudgets == null) {
                    CircularProgressIndicator()
                } else {
                    BudgetList(listBudgets!!)
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