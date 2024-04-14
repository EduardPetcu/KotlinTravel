package com.example.travel.data

import java.time.format.DateTimeFormatter

data class Expense (
    val id: String,
    val budgetId: String,
    val price: Double,
    val category: String,
    val description: String,
    val date: String,
) {
    constructor() : this(
        id = "",
        budgetId = "",
        price = 0.0,
        category = "",
        description = "",
        date = ""
    )

    constructor(budgetId: String, price: Double, category: String, description: String) : this(
        // generate a random 20 characters alphanumeric string id
        id = (1..20).map { (('a'..'z') + ('A'..'Z') + ('0'..'9')).random() }.joinToString(""),
        budgetId = budgetId,
        price = price,
        category = category,
        description = description,
        date = java.time.LocalDate.now().toString().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    )
}