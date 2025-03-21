package com.example.travel.repository

import android.util.Log
import com.example.travel.constants.UserRoles
import com.example.travel.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl : DatabaseRepository {
    val db = Firebase.firestore
    // val uid = FirebaseAuth.getInstance().currentUser!!.uid

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
    // Budget insertion
    override fun updateUserData(budgetId: String) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("users").document(uid).update("budgets", FieldValue.arrayUnion(budgetId))
            .addOnSuccessListener {
                Log.d("DatabaseRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("DatabaseRepositoryImpl", "Error adding document", e)
            }
    }
    override fun updateUserData(fields: Map<String, Any>) {
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

    override fun addUserData(user: User) {
        val newuser = hashMapOf(
            "id" to FirebaseAuth.getInstance().currentUser!!.uid,
            "email" to user.email,
            "username" to user.username,
            "userBio" to "",
            "userRole" to UserRoles.UserRoles.LEVEL0,
            "achievements" to listOf<String>(),
            "visitedCities" to listOf<String>(),
            "visitedCountries" to listOf<String>(),
            "budgets" to listOf<String>(),
            "imagePicture" to "https://firebasestorage.googleapis.com/v0/b/travel-5b2c7.appspot.com/o/ProfilePicture%2Fstandard_pfp.png?alt=media&token=db4ed18e-3ff8-4461-8d50-353be51d8dbf",
            "followedUsers" to listOf<String>()
        )
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

    // for deleting elements from an array
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

    override fun fetchUserData(onSuccess: (User) -> Unit) {
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
    override suspend fun getFollowedUsers(): List<String> {
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
    override suspend fun fetchUserInfo(): User? {
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

    suspend fun getFollowedUsers(followedUsers: List<String>): List<User> {
       val users = mutableListOf<User>()
       return try {
           val querySnapshot = db.collection("users").get().await()
           querySnapshot?.toObjects(User::class.java)
           for (document in querySnapshot!!) {
               val user = document.toObject<User>()
               if (user.username in followedUsers) {
                   users.add(user)
               }
           }
           users
       } catch (e: Exception) {
           emptyList()
       }
    }
}