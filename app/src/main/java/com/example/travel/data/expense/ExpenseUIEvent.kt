package com.example.travel.data.expense

sealed class ExpenseUIEvent {
    data class ExpensePriceChanged(val price: Double) : ExpenseUIEvent()
    data class ExpenseCategoryChanged(val category: String) : ExpenseUIEvent()
    data class ExpenseDescriptionChanged(val description: String) : ExpenseUIEvent()
    object ExpenseCreation : ExpenseUIEvent()
}