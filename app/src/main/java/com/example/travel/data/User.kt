package com.example.travel.data

data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val userBio: String,
    val userImage: String, // sau alt format
    val userRole: String,
    val username: String,
    val achievements: List<Achievement>, // sau List<String>
    val locations: List<Location>, // sau List<String>
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
}
