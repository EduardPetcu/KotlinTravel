package com.example.travel.repositories

import com.example.travel.data.Budget
import com.example.travel.data.Expense
import com.example.travel.repository.ExpenseRepository

class FakeExpenseRepository : ExpenseRepository {
    var budgets: MutableList<Budget> = mutableListOf()
    var expenses: MutableList<Expense> = mutableListOf()
    override suspend fun getExpensesFromBudgetId(budgetId: String): List<Expense>? {
        return expenses.filter { it.budgetId == budgetId }
    }

    override fun insertExpense(expense: Expense) {
        expenses.add(expense)
    }

    override fun deleteExpense(expenseId: String) {
        expenses.removeIf { it.id == expenseId }
    }
}