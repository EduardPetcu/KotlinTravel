package com.example.travel.data.rules

object Validator {

    fun validateUsername(username: String): ValidationResult {
        return ValidationResult(
            (!username.isNullOrEmpty() && username.length >= 2)
        )

    }

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty())
        )
    }

    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length >= 4)
        )
    }

}

data class ValidationResult(
    val status: Boolean = false
)