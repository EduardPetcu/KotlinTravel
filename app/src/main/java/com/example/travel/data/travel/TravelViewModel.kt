package com.example.travel.data.travel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter

class TravelViewModel : ViewModel() {

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

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
}