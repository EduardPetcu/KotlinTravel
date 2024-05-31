package com.example.travel.data

data class Budget (
    val id: String,
    val author: String,
    val name: String,
    val currency: String,
    val startDate: String,
    val endDate: String,
    val total: Double,
    var totalLeft: Double,
    val expenses: List<String>,
) {
    constructor() : this(
        id = "",
        author = "",
        name = "",
        currency = "",
        startDate = "",
        endDate = "",
        total = 0.0,
        totalLeft = 0.0,
        expenses = listOf()
    )

    // constructor generating a random id with given values
    constructor(author: String, name: String, currency: String, startDate: String, endDate: String, total: Double) : this(
        // generate a random 20 characters alphanumeric string id
        id = (1..20).map { (('0'..'9')).random() }.joinToString(""),
        author = author,
        name = name,
        currency = currency,
        startDate = startDate,
        endDate = endDate,
        total = total,
        totalLeft = total,
        expenses = listOf())

    constructor(id: String, author: String, name: String, currency: String, startDate: String, endDate: String, total: Double) : this(
        id = id,
        author = author,
        name = name,
        currency = currency,
        startDate = startDate,
        endDate = endDate,
        total = total,
        totalLeft = total,
        expenses = listOf())
}