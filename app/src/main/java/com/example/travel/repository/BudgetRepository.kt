package com.example.travel.repository

import com.example.travel.data.Budget

interface BudgetRepository {
    suspend fun getBudgetsFromUserName(userID: String): List<Budget>?
    suspend fun getBudgetById(budgetId: String): Budget?
    fun insertBudget(budget: Budget)
    fun deleteBudget(budgetId: String)
    fun updateBudget(budget: Budget)
}