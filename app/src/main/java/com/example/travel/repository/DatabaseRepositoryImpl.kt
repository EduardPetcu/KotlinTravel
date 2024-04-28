package com.example.travel.repository

import android.util.Log
import com.example.travel.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Move all the Firebase database logic here
// TODO: Make functions return status codes
class DatabaseRepositoryImpl : DatabaseRepository {
    val db = Firebase.firestore
    // val uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun getUserData(): User {
        return User()
    }

    override fun updateUserData(user: User) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("users").document(uid).set(user)
            .addOnSuccessListener {
                Log.d("DatabaseRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("DatabaseRepositoryImpl", "Error adding document", e)
            }
    }
    fun updateUserData(budgetId: String) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("users").document(uid).update("budgets", FieldValue.arrayUnion(budgetId))
            .addOnSuccessListener {
                Log.d("DatabaseRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("DatabaseRepositoryImpl", "Error adding document", e)
            }
    }
    fun updateUserData(fields: Map<String, Any>) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("users").document(uid).update(fields)
            .addOnSuccessListener {
                Log.d("DatabaseRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("DatabaseRepositoryImpl", "Error adding document", e)
            }
    }

    override fun addUserData(user: User) {
        val newuser = hashMapOf(
            "id" to FirebaseAuth.getInstance().currentUser!!.uid,
            "email" to user.email,
            "username" to user.username,
            "userBio" to "",
            "userRole" to "Novice traveller",
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

    override fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        db.collection("users").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    userList.add(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DatabaseRepositoryImpl", "Error getting documents: ", exception)
            }
        return userList
    }

    override fun deleteElement(field: String, value: String) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("users").document(uid).update(field, FieldValue.arrayRemove(value))
            .addOnSuccessListener {
                Log.d("DatabaseRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("DatabaseRepositoryImpl", "Error adding document", e)
            }
    }
}