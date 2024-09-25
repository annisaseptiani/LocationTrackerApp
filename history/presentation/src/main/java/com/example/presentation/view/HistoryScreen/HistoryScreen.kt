package com.example.presentation.view.HistoryScreen

import android.location.Geocoder
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Location
import com.example.domain.usecase.GetLocationUseCase
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel : LocationViewModel) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Histori Perjalanan") })
        },
        content = {
            Column(modifier = Modifier
                .padding(it)) {
                HistoryMapView(viewModel = viewModel)

            }
        }
    )

}

@Composable
fun HistoryMapView(viewModel: LocationViewModel) {
    val locations by viewModel.locationList.collectAsState()
    val cameraPositionState = rememberCameraPositionState()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    if (locations.isNotEmpty()) {
        val firstLocation = locations.first()
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            LatLng(firstLocation.latitude, firstLocation.longitude),
            12f
        )
    }

    GoogleMap(
        modifier = Modifier.height(250.dp),
        cameraPositionState = cameraPositionState
    ) {
        locations.forEach { location ->
            val timestamp = dateFormat.format(Date(location.timestamp))
            Marker(
                state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                title = "Timestamp: ${timestamp}"
            )
        }
    }
    Text(text = "Keterangan Lokasi", modifier = Modifier.padding(20.dp))

    HistoryListView(viewModel = viewModel)
}

@Composable
fun HistoryListView(viewModel: LocationViewModel) {
    val locations by viewModel.locationList.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(locations.size) {
            LocationItem(locations[it])
        }
    }
}

@Composable
fun LocationItem(location: Location) {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val timestamp = dateFormat.format(Date(location.timestamp))
    val message = if (location.isOnline) "Online" else "Offline"
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
    val address = addresses!![0].getAddressLine(0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)

    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Row {
                Image(imageVector = Icons.Default.LocationOn, contentDescription = "location")
                Text(text = address)
            }
            Row(horizontalArrangement = Arrangement.Absolute.SpaceAround) {
                Row {
                    Image(imageVector = Icons.Default.DateRange, contentDescription = "time")
                    Text(text = timestamp)
                }
            }
            Text(text = "Status:  ${message}")
        }
    }
}

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getLocationUseCase : GetLocationUseCase
) : ViewModel(){
    private val _locationList = MutableStateFlow<List<Location>>(emptyList())
    val locationList : StateFlow<List<Location>> get() = _locationList
    init {
        viewModelScope.launch {
            _locationList.value = getLocationUseCase.invoke().firstOrNull() ?: emptyList()
        }
    }

}