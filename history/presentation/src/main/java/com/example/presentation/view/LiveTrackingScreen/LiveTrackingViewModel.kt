package com.example.presentation.view.LiveTrackingScreen

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class LiveTrackingViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    private val _initialLocation = MutableStateFlow<LatLng?>(null)
    val initialLocation: StateFlow<LatLng?> get() = _initialLocation

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> get() = _currentLocation

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> get() = _routePoints

    private val _addressInitial = MutableStateFlow<String?>(null)
    val addressInitial: StateFlow<String?> get() = _addressInitial

    private val _addressDestination = MutableStateFlow<String?>(null)
    val addressDestination: StateFlow<String?> get() = _addressDestination

    val destinationLocation = LatLng(37.42388, -122.09974) // Hardcoded destination

    private var locationCallback: LocationCallback? = null

    init {
        fetchDestinationAddress()
        startLocationUpdates()
    }


    private fun fetchDestinationAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val addresses = geocoder.getFromLocation(
                    destinationLocation.latitude,
                    destinationLocation.longitude,
                    1
                )
                _addressDestination.value = addresses?.firstOrNull()?.getAddressLine(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions not granted; handle accordingly
            return
        }

        viewModelScope.launch {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    _initialLocation.value = latLng
                    _currentLocation.value = latLng
                    _routePoints.value = listOf(latLng)
                    fetchInitialAddress(latLng)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val loc = locationResult.lastLocation ?: return
                val latLng = LatLng(loc.latitude, loc.longitude)
                _currentLocation.value = latLng
                _routePoints.value = _routePoints.value + latLng
            }
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    private fun fetchInitialAddress(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
                )
                _addressInitial.value = addresses?.firstOrNull()?.getAddressLine(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}