package com.example.travel.data.expense

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.travel.data.Budget
import com.example.travel.data.Expense
import com.example.travel.repository.BudgetRepository
import com.example.travel.repository.BudgetRepositoryImpl
import com.example.travel.repository.ExpenseRepository
import com.example.travel.repository.ExpenseRepositoryImpl

class ExpenseViewModel(private val expenseRepository: ExpenseRepository = ExpenseRepositoryImpl(),
                       private val budgetRepositoryImpl: BudgetRepository = BudgetRepositoryImpl()) : ViewModel() {

    var allValidationsPassed = mutableStateOf(false)
    var expenseUIState = mutableStateOf(ExpenseUIState())
    var creationExpenseInProgress = mutableStateOf(false)
    fun onEvent(event: ExpenseUIEvent, budgetArg: Budget) {
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
                createExpense(budgetArg)
            }
        }
        validateExpenseData()
    }

    private fun createExpense(budgetArg: Budget) {
        creationExpenseInProgress.value = true
        val newExpense = Expense(
            budgetId = budgetArg.id,
            price = expenseUIState.value.price,
            category = expenseUIState.value.category,
            description = expenseUIState.value.description,
        )
        expenseRepository.insertExpense(newExpense).also {
            creationExpenseInProgress.value = false
            expenseUIState.value = ExpenseUIState()
        }
        var newBudget = budgetArg.copy()
        newBudget.totalLeft -= newExpense.price
        budgetRepositoryImpl.updateBudget(newBudget)
    }

    fun resetExpenseUIState() {
        expenseUIState.value = ExpenseUIState()
        allValidationsPassed.value = false
    }
    private fun validateExpenseData() {
        val isPriceValid = expenseUIState.value.price > 0
        val isCategoryValid = expenseUIState.value.category.isNotEmpty()
        val isDescriptionValid = expenseUIState.value.description.length >= 2

        allValidationsPassed.value = isPriceValid && isCategoryValid && isDescriptionValid
    }
}