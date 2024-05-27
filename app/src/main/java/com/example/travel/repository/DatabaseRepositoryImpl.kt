package com.example.travel.repository

import android.util.Log
import com.example.travel.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

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
        Log.d("DatabaseRepositoryImpl", "Fields: $fields")
        db.collection("users").document(uid).update(fields)
            .addOnSuccessListener {
                Log.d("DatabaseRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("DatabaseRepositoryImpl", "Error adding document", e)
            }
    }

    suspend fun getFollowerUsers(followedList: List<String>): List<User> {
        val db = Firebase.firestore
        val userList = mutableListOf<User>()
        return try {
            val querySnapshot = db.collection("users").get().await()
            for (document in querySnapshot) {
                val user = document.toObject<User>()
                if (user.username in followedList) {
                    userList.add(user)
                }
            }
            userList
        } catch (e: Exception) {
            Log.e("DatabaseRepositoryImpl", "Error getting users", e)
            emptyList()
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

    fun fetchUserData(onSuccess: (User) -> Unit) {
        val db = Firebase.firestore
        val userAuth = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("ProfileAvatar", "User Auth: $userAuth");
        userAuth?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    val userReceived = document.toObject<User>()
                    if (userReceived != null) {
                        onSuccess(userReceived)
                        Log.d("ProfileAvatar", "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d("ProfileAvatar", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ProfileAvatar", "get failed with ", exception)
                }
        }
    }
    suspend fun getFollowedUsers(): List<String> {
        val db = Firebase.firestore
        val userAuth = FirebaseAuth.getInstance().currentUser?.uid
        val followedList = mutableListOf<String>()
        return try {
            val documentSnapshot = userAuth?.let {
                db.collection("users").document(it).get().await()
            }

            val user = documentSnapshot?.toObject(User::class.java)
            if (user != null) {
                followedList.addAll(user.followedUsers)
            }
            followedList
        } catch (e: Exception) {
            Log.d("ProfileAvatar", "Error getting user info", e)
            followedList
        }
    }
    suspend fun fetchUserInfo(): User? {
        var userReturned = User()
        val db = Firebase.firestore
        val userAuth = FirebaseAuth.getInstance().currentUser?.uid

        return try {
            val documentSnapshot = userAuth?.let {
                db.collection("users").document(it).get().await()
            }

            documentSnapshot?.toObject(User::class.java)
        } catch (e: Exception) {
            Log.d("ProfileAvatar", "Error getting user info", e)
            null
        }
    }
}