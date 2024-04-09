package com.example.travel.data

data class User(
    val email: String,
    var userBio: String?,
    val userRole: String,
    val username: String,
    val achievements: List<String>, // sau List<String>
    val locations: List<Location>, // sau List<String>
    val city: String? = null,
    val country: String? = null,
    val visitedCities: List<String>? = null,
    val visitedCountries: List<String>? = null,
    val lat: Double? = null,
    val long: Double? = null,
    val budgets: List<String>, // contine id-uri de bugete
) {
    constructor() : this(
        email = "",
        userBio = "",
        userRole = "",
        username = "",
        achievements = listOf(),
        locations = listOf(),
        budgets = listOf())

    constructor(email: String, username: String) : this(
        email = email,
        userBio = "",
        userRole = "Novice traveller",
        username = username,
        achievements = listOf(),
        locations = listOf(),
        budgets = listOf())
}
