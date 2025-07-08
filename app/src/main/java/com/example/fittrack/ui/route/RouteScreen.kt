package com.example.fittrack.ui.route

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    viewModel: RouteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val path by viewModel.pathPoints.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val isTracking by remember { derivedStateOf { viewModel.isTracking } }
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    // Permissions to request
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Launcher to request permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val allPermissionsGranted = permissionsMap.values.all { it }
        if (allPermissionsGranted) {
            startLocationUpdates(fusedLocationClient, viewModel, cameraPositionState, context)
        } else {
            Toast.makeText(context, "Location permissions are required to use this feature.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(userLatLng, 15f)
                }
            }
        } else {
            permissionLauncher.launch(permissions)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Route Polyline
            if (path.isNotEmpty()) {
                Polyline(
                    points = path,
                    color = if (isTracking) Color(0xFF4CAF50) else Color(0xFF2196F3),
                    width = 8f
                )
            }

            // Current Location Marker
            currentLocation?.let { location ->
                Marker(
                    state = MarkerState(position = location),
                    title = "Current Location",
                    snippet = if (isTracking) "Recording route..." else "Your location"
                )
            }
        }

        // Status Card at Top
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Status Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusIndicator(isTracking = isTracking)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isTracking) "Route Recording" else "Ready to Track",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTracking) Color(0xFF4CAF50) else Color(0xFF666666)
                        )
                        Text(
                            text = if (isTracking) "Tap stop to save your route" else "Tap start to begin tracking",
                            fontSize = 12.sp,
                            color = Color(0xFF999999)
                        )
                    }
                }

                // Route Stats (when tracking)
                if (isTracking && path.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "${path.size} points",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = "Live tracking",
                            fontSize = 10.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }
        }

        // Control Button at Bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            AnimatedContent(
                targetState = isTracking,
                transitionSpec = {
                    slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                },
                modifier = Modifier.align(Alignment.Center)
            ) { tracking ->
                if (tracking) {
                    // Stop Button
                    FloatingActionButton(
                        onClick = {
                            viewModel.stopRoute()
                            fusedLocationClient.removeLocationUpdates(viewModel.locationCallback)
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .scale(1f),
                        containerColor = Color(0xFFFF5722),
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop Recording",
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "STOP",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // Start Button
                    FloatingActionButton(
                        onClick = {
                            viewModel.startRoute()
                            val fineLocationGranted = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            val coarseLocationGranted = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                            if (fineLocationGranted && coarseLocationGranted) {
                                startLocationUpdates(fusedLocationClient, viewModel, cameraPositionState, context)
                            } else {
                                permissionLauncher.launch(permissions)
                            }
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .scale(1f),
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start Recording",
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "START",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Floating Stats Card (when tracking)
        if (isTracking) {
            FloatingStatsCard(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                pathPoints = path
            )
        }
    }
}

@Composable
fun StatusIndicator(isTracking: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isTracking) 1.3f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(16.dp)
            .scale(if (isTracking) pulseScale else 1f)
            .clip(CircleShape)
            .background(
                if (isTracking)
                    Color(0xFF4CAF50)
                else
                    Color(0xFF999999)
            )
    )
}

@Composable
fun FloatingStatsCard(
    modifier: Modifier = Modifier,
    pathPoints: List<LatLng>
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Live Stats",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${pathPoints.size}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "GPS Points",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}

fun startLocationUpdates(
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: RouteViewModel,
    cameraPositionState: CameraPositionState,
    context: Context
) {
    val fineLocationGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseLocationGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (fineLocationGranted || coarseLocationGranted) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            viewModel.locationCallback,
            Looper.getMainLooper()
        )
    } else {
        Toast.makeText(context, "Location permissions are not granted.", Toast.LENGTH_LONG).show()
    }
}