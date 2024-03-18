package com.example.travel.data

data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    var userBio: String?,
    val userImage: String, // sau alt format
    val userRole: String,
    val username: String,
    val achievements: List<Achievement>, // sau List<String>
    val locations: List<Location>, // sau List<String>
    val city: String? = null,
    val country: String? = null,
    val visitedCities: List<String>? = null,
    val visitedCountries: List<String>? = null
) {
    constructor() : this(
        email = "",
        firstName = "",
        lastName = "",
        userBio = "",
        userImage = "",
        userRole = "",
        username = "",
        achievements = listOf(),
        locations = listOf())

    constructor(email: String, username: String) : this(
        email = email,
        firstName = "",
        lastName = "",
        userBio = "",
        userImage = "",
        userRole = "Novice traveller",
        username = username,
        achievements = listOf(),
        locations = listOf())
}
