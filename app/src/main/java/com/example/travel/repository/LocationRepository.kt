package com.example.travel.repository

interface LocationRepository {
    fun getCityName(): String
    fun getCountryName(): String
    fun updateLocationInfo(country: String, city: String, lat: Double, long: Double)
    fun getVisitedCities(): List<String>
    fun getVisitedCountries(): List<String>
    fun getLocationInfo(): List<String>
    fun addVisitedCity(city: String)
    fun addVisitedCountry(country: String)
}