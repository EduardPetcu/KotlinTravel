package com.example.travel.repositories

import com.example.travel.data.Budget
import com.example.travel.repository.BudgetRepository

class FakeBudgetRepository : BudgetRepository {

    var budgets: MutableList<Budget> = mutableListOf()
    override suspend fun getBudgetsFromUserName(userID: String): List<Budget>? {
        return budgets.filter { it.author == userID }
    }

    override suspend fun getBudgetById(budgetId: String): Budget? {
        return budgets.find { it.id == budgetId }
    }

    override fun insertBudget(budget: Budget) {
        budgets.add(budget)
    }

    override fun deleteBudget(budgetId: String) {
        budgets.removeIf { it.id == budgetId }
    }

    override fun updateBudget(budget: Budget) {
        val index = budgets.indexOfFirst { it.id == budget.id }
        if (index != -1) {
            budgets[index] = budget
        }
    }
}