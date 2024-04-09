package com.example.travel.repository

import com.example.travel.data.Budget

interface BudgetRepository {
    suspend fun getBudgetsFromUserName(userID: String): List<Budget>?
    fun insertBudget(budget: Budget)
}