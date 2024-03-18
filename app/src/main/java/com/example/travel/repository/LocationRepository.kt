package com.example.travel.repository

interface LocationRepository {
    fun getCityName(): String
    fun getCountryName(): String
    fun updateCityName(city: String)
    fun updateCountryName(country: String)
    fun getVisitedCities(): List<String>
    fun getVisitedCountries(): List<String>
    fun addVisitedCity(city: String)
    fun addVisitedCountry(country: String)
}