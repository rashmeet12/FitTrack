package com.example.fittrack.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object LocationHelper {

    suspend fun getLastLocation(context: Context): Location? {
        val client = LocationServices.getFusedLocationProviderClient(context)

        // Check for location permissions
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocationPermission && !hasCoarseLocationPermission) {
            // Permissions are not granted
            return null
        }

        return suspendCancellableCoroutine { cont ->
            client.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        cont.resume(location)
                    } else {
                        // If last location is null, request a new location update
                        val locationRequest = LocationRequest.Builder(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            1000L
                        ).apply {
                            setWaitForAccurateLocation(true)
                            setMinUpdateIntervalMillis(1000L)
                        }.build()

                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                client.removeLocationUpdates(this)
                                val newLocation = locationResult.lastLocation
                                cont.resume(newLocation)
                            }

                            override fun onLocationAvailability(availability: LocationAvailability) {
                                if (!availability.isLocationAvailable) {
                                    client.removeLocationUpdates(this)
                                    cont.resume(null)
                                }
                            }
                        }

                        client.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }
    }
}
