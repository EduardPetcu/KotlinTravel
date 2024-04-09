package com.example.travel.repository

import android.util.Log
import com.example.travel.data.Budget
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class BudgetRepositoryImpl : BudgetRepository {
    private val db = Firebase.firestore
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    override suspend fun getBudgetsFromUserName(userID: String): List<Budget>? {
        val budgets = mutableListOf<Budget>()
        // get all budgets from budget collection and filter by author field
        return try {
           val querySnapshot = uid?.let {
               db.collection("budgets").get().await()
           }
            querySnapshot?.toObjects(Budget::class.java)
            for (document in querySnapshot!!) {
                val budget = document.toObject<Budget>()
                if (budget.author == userID) {
                    budgets.add(budget)
                }
            }
            budgets
        } catch (e: Exception) {
            Log.e("BudgetRepositoryImpl", "Error getting budgets", e)
            null
        }
    }

    override fun insertBudget(budget: Budget) {
        db.collection("budgets").document(budget.id).set(budget)
            .addOnSuccessListener {
                Log.d("BudgetRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("BudgetRepositoryImpl", "Error adding document", e)
            }
    }
}