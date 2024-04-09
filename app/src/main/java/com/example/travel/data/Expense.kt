package com.example.travel.data

data class Expense (
    val id: String,
    val budgetId: String,
    val name: String,
    val date: String,
    val price: Double,
    val category: String? = null,
)