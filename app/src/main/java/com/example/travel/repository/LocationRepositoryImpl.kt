package com.example.travel.repository

import android.util.Log
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

    override fun getLocationInfo(): List<String> {
        var locationInfo = listOf<String>()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    locationInfo = listOf(document.getString("country").toString(), document.getString("city").toString(), document.getDouble("lat").toString(), document.getDouble("long").toString())
                }
            }
        Log.d("getLocationInfo", locationInfo.toString())
        return locationInfo
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
    override fun updateLocationInfo(country: String, city: String, lat: Double, long: Double) {
        db.collection("users").document(uid)
            .update("country", country, "city", city, "lat", lat, "long", long)
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
                val updatedCities = cities.toMutableList()
                updatedCities.add(city)
                transaction.update(userDocRef, "visitedCities", updatedCities)
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