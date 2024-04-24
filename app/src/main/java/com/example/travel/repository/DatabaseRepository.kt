package com.example.travel.repository

import com.example.travel.data.User

interface DatabaseRepository {
    fun getUserData(): User
    fun updateUserData(user: User)
    fun addUserData(user: User)
    fun getAllUsers(): List<User>
    fun deleteElement(field: String, value: String)
}