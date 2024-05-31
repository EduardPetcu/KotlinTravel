package com.example.travel.repository

import com.example.travel.data.User

interface DatabaseRepository {
    fun updateUserData(user: User)
    fun updateUserData(budgetId: String)
    fun updateUserData(fields: Map<String, Any>)
    fun addUserData(user: User)
    fun getAllUsers(): List<User>
    fun deleteElement(field: String, value: String)
    fun fetchUserData(onSuccess: (User) -> Unit)
    suspend fun getFollowedUsers(): List<String>
    suspend fun fetchUserInfo(): User?
}