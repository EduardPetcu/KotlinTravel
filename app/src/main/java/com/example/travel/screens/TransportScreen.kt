package com.example.travel.screens


import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import com.example.travel.components.ProfileContent
import com.example.travel.data.User
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TransportScreen() {
    val filename = "gadm41_ROU_1.json"
    val context = LocalContext.current
    val jsonObject = readJsonFromFile(filename, context)
    val featuresArray = jsonObject["features"]?.jsonArray
    var userInfo by remember { mutableStateOf<User?>(null) }
    val databaseRepositoryImpl = DatabaseRepositoryImpl()
    val visitedCitiesCoordinates: ArrayList<List<List<Double>>> = ArrayList()
    LaunchedEffect(key1 = true) {
        val userDeferred = async { databaseRepositoryImpl.fetchUserInfo() }
        userInfo = userDeferred.await()
    }

    if (userInfo != null) {
        for (city in userInfo!!.visitedCities!!) {
            var nameToSearch = city
            if (nameToSearch.startsWith("Județul ")) {
                nameToSearch = nameToSearch.substring(8)
            }
            if (nameToSearch.endsWith("County")) {
                nameToSearch = nameToSearch.substring(0, nameToSearch.length - 7)
            }
            val coordinates = getCoordinatesForFeature(featuresArray ?: return, nameToSearch)
            if (coordinates != null) {
                visitedCitiesCoordinates.add(coordinates)
            }
        }
    }
    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 2) },
        containerColor = BackgroundBlue,
    ) { padding ->
        ProfileContent(
            modifier = Modifier
                .padding(padding)
        ) {
            UserProfile(databaseRepositoryImpl = databaseRepositoryImpl)
            if (userInfo != null && visitedCitiesCoordinates.isNotEmpty()) {
                ComposeGoogleMap(latlng = LatLng(userInfo!!.lat!!, userInfo!!.long!!), country = userInfo!!.country!!, city = userInfo!!.city!!, visitedCitiesCoordinates = visitedCitiesCoordinates)
            }
        }
    }
}
@Composable
fun ComposeGoogleMap(latlng: LatLng, country: String, city: String, visitedCitiesCoordinates: List<List<List<Double>>>) {
    TravelTheme {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latlng, 10f)
        }
        val polygonVisible = true
        cameraPositionState.position = CameraPosition.fromLatLngZoom(latlng, 5f)
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(
                    position = latlng,
                ),
                title = city,
                snippet = country
            )
            if (polygonVisible) {
                for (coordinates in visitedCitiesCoordinates) {
                    Polygon(
                        // The coordinates are in the format [[longitude, latitude], [longitude, latitude], ...]
                        // points is a list of LatLng objects
                        points = coordinates.map { LatLng(it[1], it[0]) },
                        fillColor = Color(0x7F00FF00),
                        strokeColor = Color(0x7F000000),
                        strokeWidth = 5f
                    )
                }
            }
        }
    }
}
fun readJsonFromFile(filename: String, context: Context): JsonObject {
    val assetManager = context.assets
    val inputStream = assetManager.open(filename)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String? = reader.readLine()
    while (line != null) {
        stringBuilder.append(line).append('\n')
        line = reader.readLine()
    }

    val fileContents = stringBuilder.toString()

    // Close the reader
    reader.close()
    return Json.parseToJsonElement(fileContents).jsonObject
}

fun getCoordinatesForFeature(jsonArray: JsonArray, city_name: String): List<List<Double>>? {
    for (jsonElement in jsonArray) {
        val jsonObject = jsonElement.jsonObject
        val propertiesObject = jsonObject["properties"]?.jsonObject
        val featureName = propertiesObject?.get("NAME_1")?.jsonPrimitive?.content

        if (featureName == city_name) {
            val geometryObject = jsonObject["geometry"]?.jsonObject
            val coordinatesArray = geometryObject?.get("coordinates")?.jsonArray
            val coordinates = coordinatesArray?.mapIndexedNotNull { index, pair ->
                // skip 3/4 of the coordinates to reduce the number of points
                if (index % 4 == 0) {
                    pair.jsonArray.map { it.jsonPrimitive.double }
                } else {
                    null
                }
              //pair.jsonArray.map { coordinate -> coordinate.jsonPrimitive.double } this will take all the coordinates
            }
            return coordinates
        }
    }
    return null
}

@Preview
@Composable
fun TransportScreenPreview() {
    TransportScreen()
}