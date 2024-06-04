package com.example.travel.data

import android.graphics.Bitmap
import android.net.Uri
import com.example.travel.constants.UserRoles

data class User(
    val id: String,
    var email: String,
    var userBio: String?,
    var userRole: String,
    var username: String,
    var achievements: List<String>, // sau List<String>
    var city: String? = null,
    var country: String? = null,
    var visitedCities: List<String>,
    var visitedCountries: List<String>,
    var lat: Double? = null,
    var long: Double? = null,
    var budgets: List<String>, // contine id-uri de bugete
    var imagePicture: String? = null,
    // create locationPicture field that will store multiple images for each visited city
    var followedUsers: List<String> = listOf(),
) {
    constructor() : this(
        id = "",
        email = "",
        userBio = "",
        userRole = "",
        username = "",
        achievements = listOf(),
        budgets = listOf(),
        visitedCities = listOf(),
        visitedCountries = listOf(),
        followedUsers = listOf()
    )

    constructor(email: String, username: String, id: String) : this(
        id = id,
        email = email,
        userBio = "",
        userRole = UserRoles.UserRoles.LEVEL0,
        username = username,
        achievements = listOf(),
        budgets = listOf(),
        visitedCities = listOf(),
        visitedCountries = listOf(),
        followedUsers = listOf(username))

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
