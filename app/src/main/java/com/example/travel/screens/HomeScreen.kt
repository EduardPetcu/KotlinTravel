package com.example.travel.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel.components.CurrentLocationScreen
import com.example.travel.components.ProfileContent
import com.example.travel.components.SelectIntervalDate
import com.example.travel.components.SignOutButton
import com.example.travel.data.login.LoginViewModel
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

// TODO: Change the SignOut button into an icon that should appear on top right corner of the screen
// TODO: Hardcode some travel options for users to select from
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(loginViewModel: LoginViewModel = viewModel()) {
    val filename = "gadm41_ROU_1.json"
    val context = LocalContext.current
    val jsonObject = readJsonFromFile(filename, context)
    val featuresArray = jsonObject?.get("features")?.jsonArray
    val nameToSearch = "Arad"
    val coordinates = getCoordinatesForFeature(featuresArray ?: return, nameToSearch)
    Log.d("HomeScreen", "Number of coordinates pairs: ${coordinates?.size}")
    Log.d("HomeScreen", "Coordinates for $nameToSearch: $coordinates")

    TravelTheme {
        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 0) },
        ) { padding ->
            ProfileContent(
                modifier = Modifier
                    .padding(padding)
            ) {
                UserProfile()
                Column() {
                    SignOutButton(loginViewModel = loginViewModel)
                    CurrentLocationScreen()
                    // SelectIntervalDate("Start Date")
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

