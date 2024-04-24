package com.example.travel.data

import android.graphics.Bitmap

data class User(
    val id: String,
    val email: String,
    var userBio: String?,
    val userRole: String,
    val username: String,
    val achievements: List<String>, // sau List<String>
    val locations: List<Location>, // sau List<String>
    val city: String? = null,
    val country: String? = null,
    val visitedCities: List<String>,
    val visitedCountries: List<String>,
    val lat: Double? = null,
    val long: Double? = null,
    val budgets: List<String>, // contine id-uri de bugete
    val imagePicture: Bitmap? = null
) {
    constructor() : this(
        id = "",
        email = "",
        userBio = "",
        userRole = "",
        username = "",
        achievements = listOf(),
        locations = listOf(),
        budgets = listOf(),
        visitedCities = listOf(),
        visitedCountries = listOf())

    constructor(email: String, username: String, id: String) : this(
        id = id,
        email = email,
        userBio = "",
        userRole = "Novice traveller",
        username = username,
        achievements = listOf(),
        locations = listOf(),
        budgets = listOf(),
        visitedCities = listOf(),
        visitedCountries = listOf())

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            username,
            email,
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
