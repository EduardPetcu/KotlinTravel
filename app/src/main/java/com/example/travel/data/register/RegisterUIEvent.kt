package com.example.travel.data.register

sealed class RegisterUIEvent {

    data class EmailChanged(val email: String) : RegisterUIEvent()
    data class UsernameChanged(val username: String) : RegisterUIEvent()
    data class PasswordChanged(val password: String) : RegisterUIEvent()

    object RegisterClicked : RegisterUIEvent()
}