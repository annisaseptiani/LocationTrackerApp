package com.example.presentation.view.LiveTrackingScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.service.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LiveTrackingScreen(
    onNavigateToHistory: () -> Unit,
    viewModel : LiveTrackingViewModel
) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        else
            Manifest.permission.ACCESS_COARSE_LOCATION
    )
    
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(key1 = multiplePermissionsState) {
        Log.d("Permissions", "Requesting permissions")
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    when {
        multiplePermissionsState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                val intent = Intent(context, LocationService::class.java)
                context.startForegroundService(intent)
            }
            RealTimeMapContent(onNavigateToHistory, viewModel)
        }
        multiplePermissionsState.shouldShowRationale -> {
            Text(text = "Location permissions are needed for live tracking to work.")
        }
        !multiplePermissionsState.allPermissionsGranted -> {
            Text(text = "Please enable location permissions in settings.")
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun RealTimeMapContent(onNavigateToHistory: () -> Unit, viewModel: LiveTrackingViewModel) {
    val cameraPositionState = rememberCameraPositionState()

    val initialLocation by viewModel.initialLocation.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val routePoints by viewModel.routePoints.collectAsState()
    val addressInitial by viewModel.addressInitial.collectAsState()
    val addressDestination by viewModel.addressDestination.collectAsState()
    val destinationLocation = viewModel.destinationLocation

    LaunchedEffect(initialLocation) {
        initialLocation?.let { latLng ->
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(latLng, 11f)
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            properties = MapProperties(
                mapType = MapType.NORMAL, // Use MapType.NORMAL to ensure default road rendering
                isMyLocationEnabled = true
            )

        ) {
            initialLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Starting Point",

                )
            }

            Marker(
                state = MarkerState(position = destinationLocation),
                title = "Destination"
            )

            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Current Location",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }

            if (routePoints.isNotEmpty()) {
                Polyline(
                    points = routePoints + destinationLocation,
                    color = Color.Blue,
                    width = 5f
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopCenter)
                .background(color = Color.White, shape = RoundedCornerShape(10F))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                addressInitial?.let { addressInitial ->
                    Row {
                        Text(text = "Initial Location Address ",  modifier = Modifier.weight(1f))
                        Text(text = addressInitial, fontWeight = FontWeight.Bold,  modifier = Modifier.weight(1f))
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(10.dp), thickness = 2.dp)
                addressDestination?.let { addressDestination ->
                    Row {
                        Text(text = "Destination Address       ",  modifier = Modifier.weight(1f),)
                        Text(text = addressDestination, fontWeight = FontWeight.Bold,  modifier = Modifier.weight(1f),)
                    }

                }
            }

        }

        FloatingActionButton(
            onClick = onNavigateToHistory,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(50.dp),
            containerColor = Color.White
        ) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(text = "Lihat History", modifier = Modifier.padding(20.dp), color = Color.DarkGray)
            }
        }
    }
}
