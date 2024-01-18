package com.example.travel.data.rules

import java.util.regex.Pattern

object Validator {

    fun validateUsername(username: String): ValidationResult {
        return ValidationResult(
            (!username.isNullOrEmpty() && username.length >= 2)
        )

    }

    fun validateEmail(email: String): ValidationResult {
        val emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)

        return ValidationResult(
            email.isNotEmpty() && matcher.matches()
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