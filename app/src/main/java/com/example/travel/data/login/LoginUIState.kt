package com.example.travel.data.login

data class LoginUIState (
    var email: String = "",
    var password: String = "",

    var isEmailValid: Boolean = false,
    var isPasswordValid: Boolean = false
)