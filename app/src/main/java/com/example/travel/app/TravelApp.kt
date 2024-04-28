package com.example.travel.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.travel.data.Budget
import com.example.travel.data.User
import com.example.travel.data.travel.TravelViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.screens.budget.CalculateScreen
import com.example.travel.screens.RegisterScreen
import com.example.travel.screens.LoginScreen
import com.example.travel.screens.HomeScreen
import com.example.travel.screens.ProfileScreen
import com.example.travel.screens.TransportScreen
import com.example.travel.screens.budget.BudgetInsertScreen
import com.example.travel.screens.budget.BudgetViewScreen
import com.example.travel.screens.budget.ExpenseInsertScreen

@Composable
fun TravelApp(travelViewModel: TravelViewModel = TravelViewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.hsl(236f, 0.58f, 0.52f)
    ) {
        when (TravelAppRouter.currentScreen.value) {
            Screen.RegisterScreen -> {
                if (travelViewModel.checkForActiveSession()) {
                    HomeScreen()
                } else {
                    RegisterScreen()
                }
            }
            Screen.LoginScreen -> {
                if (travelViewModel.checkForActiveSession()) {
                    HomeScreen()
                } else {
                    LoginScreen()
                }
            }
            Screen.HomeScreen -> HomeScreen()
            Screen.CalculateScreen -> CalculateScreen()
            Screen.TransportScreen -> TransportScreen()
            Screen.ProfileScreen -> ProfileScreen(user = TravelAppRouter.idParent.value as User?)
            Screen.BudgetScreen -> BudgetInsertScreen()
            Screen.BudgetViewScreen -> BudgetViewScreen(idBudget = TravelAppRouter.idParent.value.toString())
            Screen.ExpenseInsertScreen -> ExpenseInsertScreen(budgetArg = TravelAppRouter.idParent.value as Budget)
        }
    }

}