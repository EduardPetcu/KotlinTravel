package com.example.travel.repository

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.travel.data.User
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DatabaseRepositoryImpl : DatabaseRepository {
    override fun getUserData(): User {
        TODO("Not yet implemented")
    }

    override fun updateUserData(user: User) {
        TODO("Not yet implemented")
    }

    override fun addUserData(user: User) {
        val db = Firebase.firestore;
        val newuser = hashMapOf(
            "email" to user.email,
            "username" to user.username,
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
            .set(newuser)
            .addOnSuccessListener {
                Log.d("RegisterViewModel", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("RegisterViewModel", "Error adding document", e)
            }
    }
}