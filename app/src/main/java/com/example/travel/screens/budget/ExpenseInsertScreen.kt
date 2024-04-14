package com.example.travel.screens.budget

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.R
import com.example.travel.components.DesignComponents.DropDown
import com.example.travel.components.TextInput
import com.example.travel.data.expense.ExpenseUIEvent
import com.example.travel.data.expense.ExpenseViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.InputType
import com.example.travel.ui.theme.TravelTheme

@Composable
fun ExpenseInsertScreen(expenseViewModel: ExpenseViewModel = viewModel(), idBudget: String) {
    TravelTheme {
        val context: Context = LocalContext.current
        val listCategories = listOf("Food", "Transport", "Drink", "Entertainment", "Others")
        val focusManager = LocalFocusManager.current
        BackHandler(
            onBack = {
                expenseViewModel.resetExpenseUIState()
                TravelAppRouter.navigateTo(Screen.BudgetViewScreen, idBudget)
            })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundBlue)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(
                inputType = InputType(
                    label = "Expense Description",
                    icon = Icons.Default.Description,
                    KeyboardOptions(imeAction = ImeAction.Next),
                    visualTransformation = VisualTransformation.None
                ), KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }), onTextChanged = {
                    expenseViewModel.onEvent(ExpenseUIEvent.ExpenseDescriptionChanged(it), idBudget)
                }, errorStatus = expenseViewModel.expenseUIState.value.isDescriptionValid
            )

            TextInput(
                inputType = InputType(
                    label = "Expense Price",
                    icon = Icons.Default.MonetizationOn,
                    KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
                    visualTransformation = VisualTransformation.None
                ), KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }), onTextChanged = {
                    val price = it.toDoubleOrNull() ?: 0.0
                    expenseViewModel.onEvent(ExpenseUIEvent.ExpensePriceChanged(price), idBudget)
                }, errorStatus = expenseViewModel.expenseUIState.value.isPriceValid
            )

            DropDown(onOptionSelected = {
                expenseViewModel.onEvent(ExpenseUIEvent.ExpenseCategoryChanged(it), idBudget)
            }, listedElements = listCategories)

            Button(
                onClick = {
                    expenseViewModel.onEvent(ExpenseUIEvent.ExpenseCreation, idBudget)
                },
                enabled = expenseViewModel.allValidationsPassed.value,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Add Expense")
            }
            if (expenseViewModel.creationExpenseInProgress.value) {
                CircularProgressIndicator()
            }
        }
    }
}