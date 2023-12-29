package com.example.travel.data

data class RegistrationUIState(
    var email: String = "",
    var username: String = "",
    var password: String = "",

    var isEmailValid: Boolean = false,
    var isUsernameValid: Boolean = false,
    var isPasswordValid: Boolean = false
)