package com.example.travel.repository

import com.example.travel.data.Expense

interface ExpenseRepository {
    suspend fun getExpensesFromBudgetId(budgetId: String): List<Expense>?
    fun insertExpense(expense: Expense)
    fun deleteExpense(expenseId: String)
}