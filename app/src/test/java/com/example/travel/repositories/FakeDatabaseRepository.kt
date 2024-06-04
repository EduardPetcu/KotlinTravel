package com.example.travel.repositories

import com.example.travel.data.User
import com.example.travel.repository.DatabaseRepository

class FakeDatabaseRepository : DatabaseRepository {
    var user: User = User()
    val users = mutableListOf<User>(user)
    override fun updateUserData(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
        }
    }

    override fun updateUserData(budgetId: String) {
        user.budgets += budgetId
        updateUserData(user)
    }

    override fun updateUserData(fields: Map<String, Any>) {
        fields.forEach { (key, value) ->
            when (key) {
                "email" -> user.email = value as String
                "username" -> user.username = value as String
                "userBio" -> user.userBio = value as String
                "userRole" -> user.userRole = value as String
                "achievements" -> user.achievements = value as List<String>
                "city" -> user.city = value as String
                "country" -> user.country = value as String
                "visitedCities" -> user.visitedCities = value as List<String>
                "visitedCountries" -> user.visitedCountries = value as List<String>
                "lat" -> user.lat = value as Double
                "long" -> user.long = value as Double
                "budgets" -> user.budgets = value as List<String>
                "imagePicture" -> user.imagePicture = value as String
                "followedUsers" -> user.followedUsers = value as List<String>
            }
        }
        updateUserData(user)
    }

    override fun addUserData(user: User) {
        users.add(user)
    }

    override fun getAllUsers(): List<User> {
        return users
    }

    override fun deleteElement(field: String, value: String) {
        // get the array field from the user object and remove the value
        when (field) {
            "achievements" -> user.achievements -= value
            "visitedCities" -> user.visitedCities -= value
            "visitedCountries" -> user.visitedCountries -= value
            "budgets" -> user.budgets -= value
            "followedUsers" -> user.followedUsers -= value
        }
        updateUserData(user)
    }

    override fun fetchUserData(onSuccess: (User) -> Unit) {
        // Not important for testing
    }

    override suspend fun getFollowedUsers(): List<String> {
        return user.followedUsers
    }

    override suspend fun fetchUserInfo(): User {
        return user
    }
}