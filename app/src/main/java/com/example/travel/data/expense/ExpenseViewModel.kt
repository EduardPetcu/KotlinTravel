package com.example.travel.data.expense

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.travel.data.Expense
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.repository.ExpenseRepositoryImpl

class ExpenseViewModel : ViewModel() {

    var allValidationsPassed = mutableStateOf(false)
    var expenseUIState = mutableStateOf(ExpenseUIState())
    var expenseRepository = ExpenseRepositoryImpl()
    var creationExpenseInProgress = mutableStateOf(false)
    fun onEvent(event: ExpenseUIEvent, budgetId: String) {
        when (event) {
            is ExpenseUIEvent.ExpensePriceChanged -> {
                expenseUIState.value = expenseUIState.value.copy(
                    price = event.price
                )
            }
            is ExpenseUIEvent.ExpenseCategoryChanged -> {
                expenseUIState.value = expenseUIState.value.copy(
                    category = event.category
                )
            }
            is ExpenseUIEvent.ExpenseDescriptionChanged -> {
                expenseUIState.value = expenseUIState.value.copy(
                    description = event.description
                )
            }
            is ExpenseUIEvent.ExpenseCreation -> {
                createExpense(budgetId)
            }
        }
        validateExpenseData()
    }

    private fun createExpense(budgetId: String) {
        creationExpenseInProgress.value = true
        val newExpense = Expense(
            budgetId = budgetId,
            price = expenseUIState.value.price,
            category = expenseUIState.value.category,
            description = expenseUIState.value.description,
        )
        expenseRepository.insertExpense(newExpense).also {
            creationExpenseInProgress.value = false
            expenseUIState.value = ExpenseUIState()
        }
        navigateTo(Screen.BudgetViewScreen, budgetId)
    }

    fun resetExpenseUIState() {
        expenseUIState.value = ExpenseUIState()
        allValidationsPassed.value = false
    }
    private fun validateExpenseData() {
        Log.d("ExpenseViewModel", "Price: ${expenseUIState.value.price} Category: ${expenseUIState.value.category} Description: ${expenseUIState.value.description}")
        val isPriceValid = expenseUIState.value.price > 0
        val isCategoryValid = expenseUIState.value.category.isNotEmpty()
        val isDescriptionValid = expenseUIState.value.description.length >= 2

        allValidationsPassed.value = isPriceValid && isCategoryValid && isDescriptionValid
    }
}