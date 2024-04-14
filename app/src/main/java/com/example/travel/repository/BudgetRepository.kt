package com.example.travel.repository

import com.example.travel.data.Budget

// TODO: Implement Update budget method in order to update the remaining value of a budget
interface BudgetRepository {
    suspend fun getBudgetsFromUserName(userID: String): List<Budget>?
    suspend fun getBudgetById(budgetId: String): Budget?
    fun insertBudget(budget: Budget)
    fun deleteBudget(budgetId: String)
    fun updateBudget(budget: Budget)
}