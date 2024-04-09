package com.example.travel.data.budget

sealed class BudgetUIEvent {
    data class BudgetNameChanged(val name: String) : BudgetUIEvent()
    data class BudgetCurrencyChanged(val currency: String) : BudgetUIEvent()
    data class BudgetDateChanged(val intervalDate: String) : BudgetUIEvent()
    data class BudgetTotalChanged(val total: Double) : BudgetUIEvent()
    object BudgetCreation : BudgetUIEvent()
}