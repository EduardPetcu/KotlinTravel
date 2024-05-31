package com.example.travel.data.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.travel.data.rules.Validator
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())
    var allValidationsPassed = mutableStateOf(false)
    var loginInProgress = mutableStateOf(false)

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
                printState()
            }
            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
                printState()
            }
            is LoginUIEvent.LoginClicked -> {
                login()
            }
            is LoginUIEvent.LogoutClicked -> {
                logout()
            }
        }
        validateDataWithRules()
    }

    private fun login() {
        signInUserInFirebase(
            email = loginUIState.value.email,
            password = loginUIState.value.password
        )
        loginUIState.value.email = "";
        loginUIState.value.password = "";
    }

    private fun signInUserInFirebase(email: String, password: String) {
        loginInProgress.value = true
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginInProgress.value = false
                    TravelAppRouter.navigateTo(Screen.HomeScreen)
                } else {
                    Log.w("LoginViewModel", "createUserWithEmail:failure", task.exception)
                    loginInProgress.value = false
                }
            }
    }

    private fun validateDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        loginUIState.value = loginUIState.value.copy(
            isEmailValid = emailResult.status,
            isPasswordValid = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status
    }

    fun logout() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                TravelAppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d("TravelViewModel", "User is still logged in")
            }
        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }
    private fun printState() {
        Log.d(TAG, "Login UI State: ")
        Log.d(TAG, loginUIState.value.toString())
    }
}