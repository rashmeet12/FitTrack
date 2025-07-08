package com.example.fittrack.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fittrack.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showSignOutDialog by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = hiltViewModel()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                onSignOutClick = { showSignOutDialog = true },
                onMenuItemClick = { route ->
                    scope.launch {
                        drawerState.close()
                        navController.navigate(route)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            // Main Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Header Section
                item {
                    HeaderSection(
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }

                // Week Days Selector
                item {
                    WeekDaysSelector()
                }

                // Steps Progress Card
                item {
                    StepsProgressCard()
                }

                // Quick Stats Row
                item {
                    QuickStatsRow()
                }

                // Health Actions
                item {
                    HealthActionsSection(navController)
                }

                // Fitness Actions
                item {
                    FitnessActionsSection(navController)
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            // Bottom Navigation
            BottomNavigationBar(navController)
        }
    }

    // Sign Out Confirmation Dialog
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSignOutDialog = false
                        // Call your AuthViewModel signOut function here
                         authViewModel.signOut()
                    }
                ) {
                    Text("Yes", color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSignOutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NavigationDrawerContent(
    onSignOutClick: () -> Unit,
    onMenuItemClick: (String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "FitTrack",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Stay Healthy!",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu Items
            val menuItems = listOf(
                DrawerMenuItem("Profile", Icons.Outlined.Person, "profile"),
                DrawerMenuItem("Settings", Icons.Outlined.Settings, "settings"),
                DrawerMenuItem("Statistics", Icons.Outlined.Analytics, "analytics"),
                DrawerMenuItem("Health Records", Icons.Outlined.MedicalServices, "health_records"),
                DrawerMenuItem("Achievements", Icons.Outlined.EmojiEvents, "achievements"),
                DrawerMenuItem("Privacy Policy", Icons.Outlined.Policy, "privacy"),
                DrawerMenuItem("About", Icons.Outlined.Info, "about"),
                DrawerMenuItem("Help & Support", Icons.AutoMirrored.Outlined.Help, "help")
            )

            menuItems.forEach { item ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            tint = Color(0xFF6B7280)
                        )
                    },
                    label = {
                        Text(
                            item.title,
                            color = Color(0xFF374151)
                        )
                    },
                    selected = false,
                    onClick = { onMenuItemClick(item.route) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Sign Out Button
            NavigationDrawerItem(
                icon = {
                    Icon(
                        Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Sign Out",
                        tint = Color(0xFFEF4444)
                    )
                },
                label = {
                    Text(
                        "Sign Out",
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Medium
                    )
                },
                selected = false,
                onClick = onSignOutClick,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

data class DrawerMenuItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun HeaderSection(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF6366F1)
                )
            }

            Column {
                Text(
                    text = "Good Morning!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = "Let's get moving",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF6366F1)
                )
            }
        }
    }
}

@Composable
fun WeekDaysSelector() {
    val today = LocalDate.now()
    val daysOfWeek = (0..6).map { today.minusDays((today.dayOfWeek.value - 1 - it).toLong()) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        itemsIndexed(daysOfWeek) { index, date ->
            val isToday = date == today
            val dayLetter = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).first().toString()

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isToday) Color(0xFF6366F1) else Color.White,
                        CircleShape
                    )
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayLetter,
                        color = if (isToday) Color.White else Color(0xFF6B7280),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isToday) Color.White else Color(0xFF374151),
                        fontSize = 12.sp,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun StepsProgressCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF10B981),
                            Color(0xFF059669)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Today's Steps",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Keep it up! You're doing great",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.DirectionsWalk,
                            contentDescription = "Steps",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "8,247",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "/ 10,000",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Calories",
            value = "324",
            unit = "kcal",
            icon = Icons.Default.LocalFireDepartment,
            color = Color(0xFFEF4444)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Distance",
            value = "5.2",
            unit = "km",
            icon = Icons.Default.LocationOn,
            color = Color(0xFF3B82F6)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Active",
            value = "47",
            unit = "min",
            icon = Icons.Default.Timer,
            color = Color(0xFF8B5CF6)
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = " $unit",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }
                Text(
                    text = title,
                    color = Color(0xFF6B7280),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun HealthActionsSection(navController: NavController) {
    Column {
        Text(
            text = "Health Monitoring",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HealthActionCard(
                modifier = Modifier.weight(1f),
                title = "Blood Pressure",
                icon = Icons.Default.Monitor,
                color = Color(0xFFEF4444)
            ) {
                navController.navigate("blood_pressure")
            }
            HealthActionCard(
                modifier = Modifier.weight(1f),
                title = "Blood Sugar",
                icon = Icons.Default.Bloodtype,
                color = Color(0xFF8B5CF6)
            ) {
                navController.navigate("blood_sugar")
            }
        }
    }
}

@Composable
fun FitnessActionsSection(navController: NavController) {
    Column {
        Text(
            text = "Fitness Activities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FitnessActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Workout",
                    subtitle = "Start Exercise",
                    icon = Icons.Default.FitnessCenter,
                    color = Color(0xFF10B981)
                ) {
                    navController.navigate("activity")
                }
                FitnessActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Steps",
                    subtitle = "Track Walking",
                    icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                    color = Color(0xFF3B82F6)
                ) {
                    navController.navigate("step")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FitnessActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Route",
                    subtitle = "Track Path",
                    icon = Icons.Default.Route,
                    color = Color(0xFFF59E0B)
                ) {
                    navController.navigate("route")
                }
                FitnessActionCard(
                    modifier = Modifier.weight(1f),
                    title = "BMI",
                    subtitle = "Calculate",
                    icon = Icons.Default.Scale,
                    color = Color(0xFF8B5CF6)
                ) {
                    navController.navigate("bmi")
                }
            }
        }
    }
}

@Composable
fun HealthActionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151)
            )
        }
    }
}

@Composable
fun FitnessActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color(0xFF111827)
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF6B7280),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = true,
                onClick = { }
            )
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = false,
                onClick = { navController.navigate("profile") }
            )
            BottomNavItem(
                icon = Icons.AutoMirrored.Filled.Article,
                label = "Articles",
                isSelected = false,
                onClick = { navController.navigate("articles") }
            )
            BottomNavItem(
                icon = Icons.Default.Analytics,
                label = "Analytics",
                isSelected = false,
                onClick = { navController.navigate("history") }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color(0xFF6366F1) else Color(0xFF9CA3AF)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (isSelected) color.copy(alpha = 0.1f) else Color.Transparent,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}