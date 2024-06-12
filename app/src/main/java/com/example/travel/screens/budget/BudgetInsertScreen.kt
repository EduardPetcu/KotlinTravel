package com.example.travel.screens.budget

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.components.DesignComponents.DateRange
import com.example.travel.components.DesignComponents.DropDown
import com.example.travel.components.TextInput
import com.example.travel.data.budget.BudgetUIEvent
import com.example.travel.data.budget.BudgetViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.service.ScheduleNotification
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.InputType
import com.example.travel.ui.theme.TravelTheme
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BudgetInsertScreen(budgetViewModel: BudgetViewModel = viewModel()) {
    val context: Context = LocalContext.current
    val dateRange = DateRange()
    val currencyList = listOf("RON", "USD", "EUR", "GBP", "JPY", "CNY")

    TravelTheme {
        val focusManager = LocalFocusManager.current
        BackHandler (
            onBack = {
                budgetViewModel.resetBudgetUIState()
                navigateTo(Screen.CalculateScreen)
            })
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundBlue)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(inputType = InputType.BudgetName, KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }), onTextChanged = {
                budgetViewModel.onEvent(BudgetUIEvent.BudgetNameChanged(it))
            }, errorStatus = budgetViewModel.budgetUIState.value.isNameValid)

            TextInput(inputType = InputType.BudgetAmount, KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }), onTextChanged = {input ->
                val amount = input.toDoubleOrNull() ?: 0.0
                budgetViewModel.onEvent(BudgetUIEvent.BudgetTotalChanged(amount))
            }, errorStatus = budgetViewModel.budgetUIState.value.isTotalValid)

            DropDown(onOptionSelected = {
                budgetViewModel.onEvent(BudgetUIEvent.BudgetCurrencyChanged(it))
            }, listedElements = currencyList)

            DateRangeField(dateRange, budgetViewModel)
            Button(
                onClick = {
                    budgetViewModel.onEvent(BudgetUIEvent.BudgetCreation)
                    ScheduleNotification().scheduleNotification(context, "Budget Created", dateRange.endDate, budgetId = budgetViewModel.budgetID)
                    navigateTo(Screen.CalculateScreen)
                }, modifier = Modifier.fillMaxWidth(),
                enabled = budgetViewModel.allValidationsPassed.value
            ) {
                Text(text = "Create Budget")
            }

            if (budgetViewModel.creationBudgetInProgress.value) {
                CircularProgressIndicator()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun BudgetInsertScreenPreview() {
    BudgetInsertScreen()
}

@Composable
fun DateRangeField(dateRange: DateRange, budgetViewModel: BudgetViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    val onValueChange: (String) -> Unit = {
        budgetViewModel.onEvent(BudgetUIEvent.BudgetDateChanged(it))
    }

    TextField(
        value = dateRange.startDate + " - " + dateRange.endDate,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        readOnly = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Date Range",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = {
                        showDialog = true
                    })
            )
        },
        label = {
            Text(text = "Date Range")
        },
        isError = !budgetViewModel.budgetUIState.value.isStartDateValid &&
                !budgetViewModel.budgetUIState.value.isEndDateValid,
    )

    if (showDialog) {
        dateRange.SelectIntervalDate {
            showDialog = false
        }
    }

    LaunchedEffect(dateRange.startDate, dateRange.endDate) {
        onValueChange(dateRange.startDate + " - " + dateRange.endDate)
    }
}