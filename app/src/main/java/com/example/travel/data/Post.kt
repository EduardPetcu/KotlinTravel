package com.example.travel.data

// TODO: Replace existent functions related to posted images with the new ones
data class Post(
    val id: String,
    val userId: String,
    val username: String,
    val image: String,
    val description: String?,
    val timestamp: Long,
    val location: String,
    val userProfilePicture: String?,
) {
    constructor() : this(
        id = "",
        userId = "",
        username = "",
        image = "",
        description = "",
        timestamp = 0,
        location = "",
        userProfilePicture = ""
    )
    constructor(userId: String, username: String, image: String, location: String, userProfilePicture: String?) : this(
        id = (1..20).map { (('a'..'z') + ('A'..'Z') + ('0'..'9')).random() }.joinToString(""),
        userId = userId,
        username = username,
        image = image,
        description = "",
        timestamp = System.currentTimeMillis(),
        location = location,
        userProfilePicture = userProfilePicture
    )
}