package com.example.travel.repository

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LocationRepositoryImpl : LocationRepository{

    private val db = Firebase.firestore;
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun getCityName(): String {
        var city = ""
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    city = document.getString("city").toString()
                }
            }
        return city
    }

    override fun getCountryName(): String {
        var country = ""
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    country = document.getString("country").toString()
                }
            }
        return country
    }

    override fun updateCityName(city: String) {
        db.collection("users").document(uid)
            .update("city", city)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override fun updateCountryName(country: String) {
        Log.d("updateCountryName", "Updating country to $country")
        db.collection("users").document(uid)
            .update("country", country)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override fun getVisitedCities(): List<String> {
        var cities = listOf<String>()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    cities = document.get("visitedCities") as List<String>
                }
            }
        return cities
    }

    override fun getVisitedCountries(): List<String> {
        var countries = listOf<String>()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    countries = document.get("visitedCountries") as List<String>
                }
            }
        return countries
    }

    override fun addVisitedCity(city: String) {
        if (city == "") {
            return
        }
        db.runTransaction {
            transaction ->
            val userDocRef = db.collection("users").document(uid)
            val snapshot = transaction.get(userDocRef)
            val cities = snapshot.get("visitedCities") as List<String>
            if (!cities.contains(city)) {
                val updated_cities = cities.toMutableList()
                updated_cities.add(city)
                transaction.update(userDocRef, "visitedCities", updated_cities)
            }
            null
        }
    }

    override fun addVisitedCountry(country: String) {
        if (country == "") {
            return
        }
        db.runTransaction {
            transaction ->
            val userDocRef = db.collection("users").document(uid)
            val snapshot = transaction.get(userDocRef)
            val countries = snapshot.get("visitedCountries") as List<String>
            if (!countries.contains(country)) {
                // append country to countries
                val updated_countries = countries.toMutableList()
                updated_countries.add(country)
                transaction.update(userDocRef, "visitedCountries", updated_countries)
            }
            null
        }.addOnSuccessListener {
        }.addOnFailureListener {
        }
    }
}