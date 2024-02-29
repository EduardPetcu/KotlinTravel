package com.example.travel.data.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.travel.data.RegistrationUIState
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.data.rules.Validator
import com.example.travel.navigation.Screen
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {

    private val TAG = RegisterViewModel::class.simpleName
    val db = Firebase.firestore;
    var registrationUIState = mutableStateOf(RegistrationUIState())
    var allValidationsPassed = mutableStateOf(false)
    var signUpInProgress = mutableStateOf(false)

    fun onEvent(event: RegisterUIEvent) {
        when (event) {
            is RegisterUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
                printState()
            }
            is RegisterUIEvent.UsernameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    username = event.username
                )
                printState()
            }
            is RegisterUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
                printState()
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

    private fun validateDataWithRules() {
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
                    val user = hashMapOf(
                        "email" to email,
                        "username" to registrationUIState.value.username,
                        "firstName" to "",
                        "lastName" to "",
                        "userBio" to "",
                        "userRole" to "Novice traveller",
                        "userImage" to "",
                        "achievements" to listOf<String>(),
                        "locations" to listOf<String>()
                    )
                    // Add a new document with id = firebase.auth().currentUser.uid
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("RegisterViewModel", "DocumentSnapshot added!")
                            TravelAppRouter.navigateTo(Screen.HomeScreen)
                        }
                        .addOnFailureListener { e ->
                            Log.w("RegisterViewModel", "Error adding document", e)
                        }
                } else {
                    Log.w("RegisterViewModel", "createUserWithEmail:failure", task.exception)
                }
            }

    }

    private fun printState() {
        Log.d(TAG, "Registration UI State: ")
        Log.d(TAG, registrationUIState.value.toString())
    }
}