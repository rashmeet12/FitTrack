package com.example.fittrack.ui.profile

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    navController: NavController,
    viewModel: ProfileSetupViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showImageOptions by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Debug logs to check state changes
    LaunchedEffect(uiState.dateOfBirth) {
        Log.d("ProfileSetup", "Date of birth changed: ${uiState.dateOfBirth}")
    }

    LaunchedEffect(showDatePicker) {
        Log.d("ProfileSetup", "Show date picker: $showDatePicker")
    }

    // Create a temporary file for camera capture
    val imageFile = remember {
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile_${System.currentTimeMillis()}.jpg")
    }

    val imageUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    // Camera launcher - NO PERMISSION NEEDED for ActivityResultContracts.TakePicture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.updateProfileImage(imageUri)
        }
    }

    // Gallery launcher for images (fallback for older Android versions)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it) }
    }

    // Visual media picker for Android 13+
    val visualMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it) }
    }

    // Permission launcher for READ_MEDIA_IMAGES on Android 13+
    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                visualMediaLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                galleryLauncher.launch("image/*")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFE3F2FD)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Text(
                text = "Complete Your Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Help us personalize your fitness journey",
                fontSize = 16.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 32.dp)
            )

            // Profile Picture Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Profile Picture",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(
                                BorderStroke(2.dp, Color(0xFF1976D2)),
                                CircleShape
                            )
                            .clickable { showImageOptions = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.profileImageUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(uiState.profileImageUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Photo",
                                    tint = Color(0xFF1976D2),
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    text = "Add Photo",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1976D2),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Personal Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Personal Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Display Name
                    OutlinedTextField(
                        value = uiState.displayName,
                        onValueChange = viewModel::updateDisplayName,
                        label = { Text("Display Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF1976D2)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            focusedLabelColor = Color(0xFF1976D2)
                        )
                    )

                    // Date of Birth - FIXED VERSION
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable {
                                Log.d("ProfileSetup", "Date card clicked")
                                showDatePicker = true
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF1976D2))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Column {
                                Text(
                                    text = "Date of Birth",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1976D2)
                                )
                                Text(
                                    text = uiState.dateOfBirth?.let {
                                        try {
                                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                                        } catch (e: Exception) {
                                            Log.e("ProfileSetup", "Date formatting error", e)
                                            "Invalid date"
                                        }
                                    } ?: "Select your date of birth",
                                    fontSize = 16.sp,
                                    color = if (uiState.dateOfBirth != null) Color.Black else Color.Gray
                                )
                            }
                        }
                    }

                    // Age (Auto-calculated)
                    if (uiState.age != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE3F2FD)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Age: ${uiState.age} years",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1976D2)
                                )
                            }
                        }
                    }

                    // Gender Selection
                    Text(
                        text = "Gender",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Male", "Female", "Other").forEach { gender ->
                            FilterChip(
                                selected = uiState.gender == gender,
                                onClick = { viewModel.updateGender(gender) },
                                label = { Text(gender) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF1976D2),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Physical Information Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Physical Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.weightKg?.toString() ?: "",
                            onValueChange = { viewModel.updateWeightKg(it.toFloatOrNull()) },
                            label = { Text("Weight (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1976D2),
                                focusedLabelColor = Color(0xFF1976D2)
                            )
                        )

                        OutlinedTextField(
                            value = uiState.heightCm?.toString() ?: "",
                            onValueChange = { viewModel.updateHeightCm(it.toFloatOrNull()) },
                            label = { Text("Height (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1976D2),
                                focusedLabelColor = Color(0xFF1976D2)
                            )
                        )
                    }
                }
            }

            // Fitness Goals Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Fitness Goals",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val goals = listOf("Weight Loss", "Muscle Gain", "Maintenance", "General Fitness")
                    goals.forEach { goal ->
                        FilterChip(
                            selected = uiState.fitnessGoal == goal,
                            onClick = { viewModel.updateFitnessGoal(goal) },
                            label = { Text(goal) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1976D2),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Activity Level Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Activity Level",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val activityLevels = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active")
                    activityLevels.forEach { level ->
                        FilterChip(
                            selected = uiState.activityLevel == level,
                            onClick = { viewModel.updateActivityLevel(level) },
                            label = { Text(level) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1976D2),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Complete Profile Button
            Button(
                onClick = { viewModel.completeProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Complete Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Date Picker Dialog - FIXED VERSION
    if (showDatePicker) {
        Log.d("ProfileSetup", "Showing date picker dialog")

        // Create fresh date picker state each time
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.dateOfBirth ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = {
                Log.d("ProfileSetup", "Date picker dismissed")
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Log.d("ProfileSetup", "Date picker confirmed")
                        datePickerState.selectedDateMillis?.let { millis ->
                            Log.d("ProfileSetup", "Selected date: $millis")
                            viewModel.updateDateOfBirth(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        Log.d("ProfileSetup", "Date picker cancelled")
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,

            )
        }
    }

    // Image selection dialog
    if (showImageOptions) {
        AlertDialog(
            onDismissRequest = { showImageOptions = false },
            title = { Text("Select Profile Picture") },
            text = { Text("Choose how you want to add your profile picture") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImageOptions = false
                        // Handle gallery selection with proper permissions
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // Check if we need READ_MEDIA_IMAGES permission
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.READ_MEDIA_IMAGES
                                ) -> {
                                    visualMediaLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                else -> {
                                    mediaPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                                }
                            }
                        } else {
                            // For older versions, use GetContent which doesn't require permission
                            galleryLauncher.launch("image/*")
                        }
                    }
                ) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showImageOptions = false
                        // Camera doesn't need permission with TakePicture contract
                        cameraLauncher.launch(imageUri)
                    }
                ) {
                    Text("Camera")
                }
            }
        )
    }

    // Handle navigation after profile completion
    LaunchedEffect(uiState.isProfileCompleted) {
        if (uiState.isProfileCompleted) {
            navController.navigate("home") {
                popUpTo("profile_setup") { inclusive = true }
            }
        }
    }
}