package com.example.travel.data.travel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter

class TravelViewModel : ViewModel() {

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun signout() {
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

    fun checkForActiveSession(): Boolean {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d("TravelViewModel", "User is logged in")
            isUserLoggedIn.value = true
            return true
        } else {
            Log.d("TravelViewModel", "User is not logged in")
            isUserLoggedIn.value = false
            return false
        }
    }

    val emailid: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        FirebaseAuth.getInstance().currentUser?.also {
            it.email?.also {
                email -> emailid.value = email
            }
        }
    }
}