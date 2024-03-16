//package com.example.travel.data.location
//
//import android.location.Location
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.travel.domain.location.LocationTracker
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class LocationViewModel @Inject constructor(
//    private val locationTracker: LocationTracker
//) : ViewModel() {
//
//    var currentLocation by mutableStateOf<Location?>(null)
//
//    fun getCurrentLocation() {
//        viewModelScope.launch {
//            currentLocation = locationTracker.getCurrentLocation()
//        }
//    }
//}