package com.example.fittrack.ui.article

// ArticlesScreen.kt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fittrack.R

// Data class for Article - imageRes remains Int for drawable resources
data class Article(
    val id: Int,
    val title: String,
    val author: String,
    val date: String,
    val readTime: String,
    val imageRes: Int, // Drawable resource ID
    val category: String,
    val content: String,
    val isBookmarked: Boolean = false
)

// Sample dummy data with actual drawable resource IDs
val sampleArticles = listOf(
    Article(
        id = 1,
        title = "A 7-Minute Core Workout for Absolute Beginners.",
        author = "Dr. Karen Asp",
        date = "June 30th",
        readTime = "8 min read",
        imageRes = R.drawable.a1, // Replace with your actual drawable
        category = "Strength Training",
        content = """
            Nearly every movement you make calls on the muscles of your midsection (aka your core), from walking your dog to squatting with a heavy load to simply reaching for a glass of water. If you haven't been working out these muscles (or haven't in a while) try this absolute-beginner-friendly workout.

            "The core is where all movement begins," says Maricris Lapaix, a National Academy of Sports Medicine (NASM) certified personal trainer in Los Angeles who leads workouts on the Centr app.

            The core, which comprises several abdominal muscles and the muscles in your pelvic floor, spine, and hips, stabilizes and controls the pelvis and spine and, by extension, affects upper- and lower-body movements.

            Are you ready to start strengthening your abdominal muscles? This workout includes five exercises that are designed (by Lapaix) for absolute beginners.

            ## The 5 Core Exercises

            ### 1. Dead Bug
            Lie faceup on the floor with your arms extended, reaching straight from your shoulders to the ceiling. Bend and raise your knees so they form a 90-degree angle, shins parallel to the floor. Brace your abdominals and press your lower back into the floor.

            Then, extend your left leg straight in front of you and your right arm behind you, so that the extended limbs hover a few inches off the ground. Return to the starting position. Repeat with your right leg and left arm, and then continue alternating sides.

            ### 2. Glute Bridge
            Lie on your back with your arms down by your sides. Bend your knees and place both feet flat on the floor, hip-width apart. Your heels should be about six to eight inches away from your glutes and toes pointed forward.

            Engage your abdominals and squeeze your glutes to lift your hips toward the ceiling. Only lift your hips as high as you can without arching your back; your body should form a straight line from your knees to your hips to your shoulders.

            ### 3. Bird Dog
            Start with hands and knees on the floor; stack your shoulders over your wrists and your hips over your knees. Gaze toward the floor so that your neck aligns with your spine, forming a straight line from the crown of your head to your hips.

            While keeping your core engaged and stable, raise your left arm straight in front of you and extend your right leg straight out behind you, reaching both away from the body and parallel to the floor.

            ### 4. Bear Plank With Knee Taps
            Start with hands and knees on the floor; stack your shoulders over your wrists and your hips over your knees. Press your palms into the floor and engage your abdominal muscles by pulling your belly button in toward your spine.

            Keep your abdominals tight as you lift your knees about an inch off the floor. Hold this position as you alternate tapping the floor with one knee.

            ### 5. Modified Side Plank
            Lie on the floor on your right side and bend your knees so your feet are behind you. Place your right forearm on the ground with your elbow underneath your shoulder.

            Brace your core and push off the ground so your upper body is supported by your right arm and knee. Then, lift your hips; your body should form a straight line from head to knee.

            ## Tips for Success

            Remember to breathe throughout each exercise and focus on proper form rather than speed. Start with 30 seconds for each exercise and gradually increase as you get stronger.

            This beginner-friendly routine will help you build a solid foundation for more advanced core training in the future.
        """.trimIndent()),
    Article(
        id = 2,
        title = "HIIT vs Steady State: Which Burns More Fat?",
        author = "Mike Chen, CPT",
        date = "Dec 3rd",
        readTime = "6 min read",
        imageRes = R.drawable.a2, // Replace with your actual drawable
        category = "Cardio",
        content = "The debate between High-Intensity Interval Training (HIIT) and steady-state cardio has been ongoing in the fitness community..."
    ),
    Article(
        id = 3,
        title = "Nutrition Timing: Does When You Eat Matter?",
        author = "Emma Rodriguez, RD",
        date = "Dec 2nd",
        readTime = "7 min read",
        imageRes = R.drawable.a3, // Replace with your actual drawable
        category = "Nutrition",
        content = "Meal timing has become a hot topic in the fitness world. From intermittent fasting to post-workout nutrition windows..."
    ),
    Article(
        id = 4,
        title = "Sleep and Recovery: The Missing Link in Your Fitness Journey",
        author = "Dr. Alex Thompson",
        date = "Dec 1st",
        readTime = "9 min read",
        imageRes = R.drawable.a4, // Replace with your actual drawable
        category = "Recovery",
        content = "Quality sleep is often overlooked in fitness routines, yet it's crucial for muscle recovery, hormone regulation..."
    ),
    Article(
        id = 5,
        title = "Home Workouts: Effective Exercises with Minimal Equipment",
        author = "Lisa Park, CPT",
        date = "Nov 30th",
        readTime = "5 min read",
        imageRes = R.drawable.a5, // Replace with your actual drawable
        category = "Home Fitness",
        content = "Don't have access to a gym? No problem! These bodyweight and minimal equipment exercises..."
    ),
    Article(
        id = 6,
        title = "Supplements Decoded: What Actually Works?",
        author = "Dr. James Wilson",
        date = "Nov 29th",
        readTime = "10 min read",
        imageRes = R.drawable.a1, // Replace with your actual drawable
        category = "Supplements",
        content = "The supplement industry is worth billions, but which products are actually backed by science?..."
    ),
    Article(
        id = 7,
        title = "Flexibility and Mobility: Why They're Crucial for Athletes",
        author = "Rebecca Martinez, PT",
        date = "Nov 28th",
        readTime = "6 min read",
        imageRes = R.drawable.a3, // Replace with your actual drawable
        category = "Mobility",
        content = "Flexibility and mobility work isn't just for yoga enthusiasts. Learn why these components are essential..."
    ),
    Article(
        id = 8,
        title = "Mental Health and Exercise: The Powerful Connection",
        author = "Dr. Kevin Park",
        date = "Nov 27th",
        readTime = "8 min read",
        imageRes = R.drawable.a2, // Replace with your actual drawable
        category = "Mental Health",
        content = "Exercise isn't just good for your body—it's a powerful tool for mental health..."
    ),
    Article(
        id = 9,
        title = "Progressive Overload: The Key to Continuous Gains",
        author = "Tom Anderson, CPT",
        date = "Nov 26th",
        readTime = "7 min read",
        imageRes = R.drawable.a5, // Replace with your actual drawable
        category = "Training",
        content = "Progressive overload is the fundamental principle behind all successful training programs..."
    ),
    Article(
        id = 10,
        title = "Hydration and Performance: More Than Just Water",
        author = "Dr. Maria Gonzalez",
        date = "Nov 25th",
        readTime = "5 min read",
        imageRes = R.drawable.a4, // Replace with your actual drawable
        category = "Hydration",
        content = "Proper hydration goes beyond drinking water. Discover how electrolytes, timing, and individual needs..."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    navController: NavController
) {
    val categories = listOf("All", "Strength Training", "Cardio", "Nutrition", "Recovery", "Training")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredArticles = if (selectedCategory == "All") {
        sampleArticles
    } else {
        sampleArticles.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Articles",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A202C),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { /* Search functionality */ }
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_search),
                            contentDescription = "Search",
                            tint = Color(0xFF4A5568)
                        )
                    }
                }

                // Category tabs
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            text = category,
                            isSelected = category == selectedCategory,
                            onSelected = { selectedCategory = category }
                        )
                    }
                }
            }
        }

        // Articles List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredArticles) { article ->
                ArticleCard(
                    article = article,
                    onArticleClick = {
                        navController.navigate("article_detail/${article.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable { onSelected() }
            .clip(RoundedCornerShape(20.dp)),
        color = if (isSelected) Color(0xFF667EEA) else Color.White,
        shape = RoundedCornerShape(20.dp),
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color(0xFF4A5568),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun ArticleCard(
    article: Article,
    onArticleClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onArticleClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Image from drawable resources
            Image(
                painter = painterResource(id = article.imageRes),
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Category badge
                Surface(
                    color = Color(0xFFEDF2F7),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = article.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color(0xFF4A5568),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = article.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A202C),
                    lineHeight = 24.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Author and meta info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = article.author,
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFF718096)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = article.date,
                                fontSize = 12.sp,
                                color = Color(0xFF718096)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFF718096)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = article.readTime,
                                fontSize = 12.sp,
                                color = Color(0xFF718096)
                            )
                        }
                    }

                    IconButton(
                        onClick = { /* Bookmark functionality */ }
                    ) {
                        Icon(
                            Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Color(0xFF4A5568)
                        )
                    }
                }
            }
        }
    }
}

// Article Detail Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: Article,
    navController: NavController
) {
    var isBookmarked by remember { mutableStateOf(article.isBookmarked) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1A202C)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { isBookmarked = !isBookmarked }
                ) {
                    Icon(
                        Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Color(0xFF667EEA) else Color(0xFF4A5568)
                    )
                }

                IconButton(
                    onClick = { /* Share functionality */ }
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF4A5568)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                // Hero image from drawable resources
                Image(
                    painter = painterResource(id = article.imageRes),
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Category
                Surface(
                    color = Color(0xFFEDF2F7),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = article.category,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                        color = Color(0xFF4A5568),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = article.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A202C),
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Author and meta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = article.author,
                            fontSize = 16.sp,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF718096)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = article.date,
                                fontSize = 14.sp,
                                color = Color(0xFF718096)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF718096)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = article.readTime,
                                fontSize = 14.sp,
                                color = Color(0xFF718096)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(color = Color(0xFFE2E8F0))

                Spacer(modifier = Modifier.height(24.dp))

                // Article content
                Text(
                    text = article.content,
                    fontSize = 16.sp,
                    color = Color(0xFF2D3748),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Add more dummy content to make it feel like a real article
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n\nDuis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n\nSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                    fontSize = 16.sp,
                    color = Color(0xFF2D3748),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}