package com.example.fittrack.ui.profile


import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Theme Colors
private val PrimaryTeal = Color(0xFF00BCD4)
private val SecondaryTeal = Color(0xFF4DD0E1)
private val DarkTeal = Color(0xFF0097A7)
private val LightTeal = Color(0xFFB2EBF2)
private val AccentGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onEditProfile: () -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryTeal,
                        SecondaryTeal,
                        Color.White
                    ),
                    startY = 0f,
                    endY = 800f
                )
            )
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = onEditProfile) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Profile Header Card
                ProfileHeaderCard(
                    displayName = uiState.displayName,
                    profileImageUri = uiState.profileImageUri,
                    onImageClick = { /* Handle image change */ }
                )
            }

            item {
                // Personal Information Card
                PersonalInfoCard(
                    email = "user@example.com", // You might want to add this to your UI state
                    dateOfBirth = uiState.dateOfBirth,
                    age = uiState.age,
                    gender = uiState.gender
                )
            }

            item {
                // Physical Stats Card
                PhysicalStatsCard(
                    weight = uiState.weightKg,
                    height = uiState.heightCm
                )
            }

            item {
                // Fitness Goals Card
                FitnessGoalsCard(
                    fitnessGoal = uiState.fitnessGoal,
                    activityLevel = uiState.activityLevel
                )
            }

            item {
                // Action Items Card
                ActionItemsCard(
                    onChangePassword = { /* Handle password change */ },
                    onMyOrders = { /* Handle orders */ },
                    onHelp = { /* Handle help */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    displayName: String,
    profileImageUri: Uri?,
    onImageClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(LightTeal)
                    .border(4.dp, PrimaryTeal, CircleShape)
                    .clickable { onImageClick() }
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profileImageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Profile",
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center),
                        tint = PrimaryTeal
                    )
                }

                // Camera icon overlay
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(DarkTeal)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change Photo",
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Name
            Text(
                text = displayName.takeIf { it.isNotBlank() } ?: "User Name",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTeal,
                textAlign = TextAlign.Center
            )

            Text(
                text = "FitTrack Member",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PersonalInfoCard(
    email: String,
    dateOfBirth: Long?,
    age: Int?,
    gender: String?
) {
    ProfileCard(title = "Personal Information") {
        ProfileInfoRow(
            icon = Icons.Default.Email,
            label = "Email",
            value = email
        )

        ProfileInfoRow(
            icon = Icons.Default.DateRange,
            label = "Date of Birth",
            value = dateOfBirth?.let {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
            } ?: "Not set"
        )

        ProfileInfoRow(
            icon = Icons.Default.Cake,
            label = "Age",
            value = age?.let { "$it years" } ?: "Not set"
        )

        ProfileInfoRow(
            icon = Icons.Default.Person,
            label = "Gender",
            value = gender ?: "Not set"
        )
    }
}

@Composable
private fun PhysicalStatsCard(
    weight: Float?,
    height: Float?
) {
    ProfileCard(title = "Physical Stats") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Default.FitnessCenter,
                label = "Weight",
                value = weight?.let { "${it.toInt()} kg" } ?: "Not set",
                color = AccentGreen
            )

            StatItem(
                icon = Icons.Default.Height,
                label = "Height",
                value = height?.let { "${it.toInt()} cm" } ?: "Not set",
                color = PrimaryTeal
            )
        }
    }
}

@Composable
private fun FitnessGoalsCard(
    fitnessGoal: String?,
    activityLevel: String?
) {
    ProfileCard(title = "Fitness Goals") {
        ProfileInfoRow(
            icon = Icons.Default.Flag,
            label = "Fitness Goal",
            value = fitnessGoal ?: "Not set"
        )

        ProfileInfoRow(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            label = "Activity Level",
            value = activityLevel ?: "Not set"
        )
    }
}

@Composable
private fun ActionItemsCard(
    onChangePassword: () -> Unit,
    onMyOrders: () -> Unit,
    onHelp: () -> Unit
) {
    ProfileCard(title = "Actions") {
        ActionItem(
            icon = Icons.Default.Lock,
            label = "Change Password",
            color = Color(0xFFE91E63),
            onClick = onChangePassword
        )

        ActionItem(
            icon = Icons.Default.ShoppingCart,
            label = "My Orders",
            color = AccentGreen,
            onClick = onMyOrders
        )

        ActionItem(
            icon = Icons.AutoMirrored.Filled.Help,
            label = "Help",
            color = Color(0xFF9C27B0),
            onClick = onHelp
        )
    }
}

@Composable
private fun ProfileCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTeal,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            content()
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = PrimaryTeal,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}