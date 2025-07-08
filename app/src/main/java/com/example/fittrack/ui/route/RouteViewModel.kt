package com.example.fittrack.ui.route

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.CurrentUserPreference
import com.example.fittrack.repository.RouteRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val routeRepo: RouteRepository,
    private val currentUserPref: CurrentUserPreference
) : ViewModel() {
    var isTracking by mutableStateOf(false)
        private set

    private val _pathPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val pathPoints: StateFlow<List<LatLng>> = _pathPoints

    fun addPoint(latLng: LatLng) {
        _pathPoints.value += latLng
    }
    private var startTime: Long = 0


    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    // Update your locationCallback to also set current location
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { loc ->
                val latLng = LatLng(loc.latitude, loc.longitude)
                _currentLocation.value = latLng // Add this line
                addPoint(latLng)
            }
        }
    }



    fun startRoute() {
        _pathPoints.value = emptyList()
        isTracking = true
        startTime = System.currentTimeMillis()
    }
    fun stopRoute() {
        isTracking = false
        val endTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            currentUserPref.userIdFlow.collect { uid ->
                uid?.let {
                    routeRepo.addRoute(it, startTime, endTime, pathPoints)
                }
            }
        }

    }

}
