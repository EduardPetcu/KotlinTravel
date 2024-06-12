package com.example.travel.repository

import android.util.Log
import com.example.travel.data.Expense
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ExpenseRepositoryImpl : ExpenseRepository{
    private val db = Firebase.firestore

    override suspend fun getExpensesFromBudgetId(budgetId: String): List<Expense>? {
        val expenses = mutableListOf<Expense>()
        // get all expenses from expense collection and filter by budgetId field
        return try {
            val querySnapshot = db.collection("expenses").get().await()
            querySnapshot?.toObjects(Expense::class.java)
            for (document in querySnapshot!!) {
                val expense = document.toObject<Expense>()
                Log.d("ExpenseRepositoryImpl", "Expense: $expense")
                if (expense.budgetId == budgetId) {
                    expenses.add(expense)
                }
            }
            expenses
        } catch (e: Exception) {
            Log.e("ExpenseRepositoryImpl", "Error getting expenses", e)
            null
        }
    }
    override fun insertExpense(expense: Expense) {
        db.collection("expenses").document(expense.id).set(expense)
            .addOnSuccessListener {
                Log.d("ExpenseRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("ExpenseRepositoryImpl", "Error adding document", e)
            }
    }

    override fun deleteExpense(expenseId: String) {
        db.collection("expenses").document(expenseId).delete()
            .addOnSuccessListener {
                Log.d("ExpenseRepositoryImpl", "DocumentSnapshot deleted!")
            }
            .addOnFailureListener { e ->
                Log.w("ExpenseRepositoryImpl", "Error deleting document", e)
            }
    }
}