package com.example.travel.components

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.travel.repository.LocationRepositoryImpl
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionBox(
    modifier: Modifier = Modifier,
    permissions: List<String>,
    requiredPermissions: List<String> = permissions,
    description: String? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    onGranted: @Composable BoxScope.(List<String>) -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions) {
    }
    val allRequiredPermissionsGranted =
        permissionState.revokedPermissions.none { it.permission in requiredPermissions }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentAlignment = if (allRequiredPermissionsGranted) {
            contentAlignment
        } else {
            Alignment.Center
        },
    ) {
        if (allRequiredPermissionsGranted) {
            onGranted(
                permissionState.permissions
                    .filter { it.status.isGranted }
                    .map { it.permission },
            )
        } else {
            PermissionScreen(
                permissionState,
                description,
            )

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionScreen(
    state: MultiplePermissionsState,
    description: String?,
)
{
    var showRationale by remember(state) {
        mutableStateOf(false)
    }

    val permissions = remember(state.revokedPermissions) {
        state.revokedPermissions.joinToString("\n") {
            " - " + it.permission.removePrefix("android.permission.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp),
            )
        }

        LaunchedEffect(Unit) {
            if (state.shouldShowRationale) {
                showRationale = true
            } else {
                state.launchMultiplePermissionRequest()
            }
        }
    }
    if (showRationale) {
        AlertDialog(
            onDismissRequest = {
                showRationale = false
            },
            title = {
                Text(text = "Permissions required by the application")
            },
            text = {
                Text(text = "Travel Planner requires the following permissions:\n $permissions")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationale = false
                        state.launchMultiplePermissionRequest()
                    },
                ) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationale = false
                    },
                ) {
                    Text("Dismiss")
                }
            },
        )
    }
}

@SuppressLint("MissingPermission")
@Composable
fun CurrentLocationScreen() {
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
            CurrentLocationContent(
                usePreciseLocation = it.contains(Manifest.permission.ACCESS_FINE_LOCATION),
            )
        },
    )
}

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)

@Composable
fun CurrentLocationContent(usePreciseLocation: Boolean) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationRepositoryImpl = LocationRepositoryImpl()
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val geocoder = remember {
        Geocoder(context, Locale.getDefault())
    }

    val cityCountry = getCityAndCountry(scope, usePreciseLocation, locationClient, geocoder)
    val city = cityCountry[0]
    val country = cityCountry[1]
    val lat = cityCountry[2].toDouble()
    val long = cityCountry[3].toDouble()
    locationRepositoryImpl.addVisitedCity(city)
    locationRepositoryImpl.addVisitedCountry(country)
    locationRepositoryImpl.updateLocationInfo(country, city, lat, long)

}

@SuppressLint("MissingPermission")
@Composable
fun getCityAndCountry(scope: CoroutineScope, usePreciseLocation: Boolean, locationClient: FusedLocationProviderClient, geocoder: Geocoder) : List<String> {
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var long by remember { mutableDoubleStateOf(0.0) }
    LaunchedEffect (Unit)  {
        scope.launch(Dispatchers.IO) {
            val priority = if (usePreciseLocation) {
                Priority.PRIORITY_HIGH_ACCURACY
            } else {
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            }
            var resultLastLocation = locationClient.lastLocation.await()
            if (resultLastLocation == null) {
                resultLastLocation = locationClient.getCurrentLocation(
                    priority,
                    CancellationTokenSource().token,
                ).await()
            }

            resultLastLocation?.let { fetchedLocation ->
                lat = fetchedLocation.latitude
                long = fetchedLocation.longitude
                val address = geocoder.getFromLocation(fetchedLocation.latitude, fetchedLocation.longitude, 1)
                if (!address.isNullOrEmpty()) {
                    Log.d("CurrentLocationScreen", "Address: ${address[0]}")
                    city = address[0].adminArea
                    country = address[0].countryName
                }

            }
        }
    }
    return listOf(city, country, lat.toString(), long.toString())
}