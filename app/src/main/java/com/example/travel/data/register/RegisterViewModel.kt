package com.example.travel.data.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.travel.data.User
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.data.rules.Validator
import com.example.travel.navigation.Screen
import com.example.travel.repository.DatabaseRepository
import com.example.travel.repository.DatabaseRepositoryImpl
class RegisterViewModel(private val databaseRepositoryImpl: DatabaseRepository = DatabaseRepositoryImpl()) : ViewModel() {

    var registrationUIState = mutableStateOf(RegistrationUIState())
    var allValidationsPassed = mutableStateOf(false)
    var signUpInProgress = mutableStateOf(false)
    fun onEvent(event: RegisterUIEvent) {
        when (event) {
            is RegisterUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
            }
            is RegisterUIEvent.UsernameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    username = event.username
                )
            }
            is RegisterUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
            }
            is RegisterUIEvent.RegisterClicked -> {
                signUp()
            }
        }
        validateDataWithRules()
    }

    private fun signUp() {
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password
        )
    }

    fun validateDataWithRules() {
        val userResult = Validator.validateUsername(
            username = registrationUIState.value.username
        )

        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )

        registrationUIState.value = registrationUIState.value.copy(
            isUsernameValid = userResult.status,
            isEmailValid = emailResult.status,
            isPasswordValid = passwordResult.status
        )

        allValidationsPassed.value = userResult.status && emailResult.status && passwordResult.status
    }

    private fun createUserInFirebase(email: String, password: String) {
        signUpInProgress.value = true
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RegisterViewModel", "createUserWithEmail:success")
                    TravelAppRouter.navigateTo(Screen.HomeScreen)
                    val user = User(email, registrationUIState.value.username, FirebaseAuth.getInstance().currentUser!!.uid)
                    databaseRepositoryImpl.addUserData(user)
                    signUpInProgress.value = false
                } else {
                    Log.w("RegisterViewModel", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}