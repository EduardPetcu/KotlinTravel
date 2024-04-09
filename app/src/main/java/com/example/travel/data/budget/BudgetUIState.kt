package com.example.travel.data.budget

data class BudgetUIState(
    var name: String = "",
    var currency: String = "",
    var total: Double = 0.0,
    var startDate: String = "",
    var endDate: String = "",

    var isNameValid: Boolean = false,
    var isCurrencyValid: Boolean = false,
    var isTotalValid: Boolean = false,
    var isStartDateValid: Boolean = false,
    var isEndDateValid: Boolean = false
)