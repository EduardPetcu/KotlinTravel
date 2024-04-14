package com.example.travel.data.expense

data class ExpenseUIState (
    var price: Double = 0.0,
    var category: String = "",
    var description: String = "",
    var date: String = "",

    var isPriceValid: Boolean = false,
    var isCategoryValid: Boolean = false,
    var isDescriptionValid: Boolean = false,
)