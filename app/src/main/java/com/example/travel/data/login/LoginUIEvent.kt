package com.example.travel.data.login

sealed class LoginUIEvent {
    data class EmailChanged(val email: String) : LoginUIEvent()
    data class PasswordChanged(val password: String) : LoginUIEvent()
    object LoginClicked : LoginUIEvent()
    object LogoutClicked : LoginUIEvent()
}