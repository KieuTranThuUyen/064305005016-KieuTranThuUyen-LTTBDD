package com.example.uthnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.ExperimentalFoundationApi

// ==================== MAIN ACTIVITY ====================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

// ==================== NAVIGATION ====================
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}

// ==================== HOME SCREEN ====================
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to Home!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007AFF)
        )
    }
}

// ==================== ONBOARDING PAGE CLASS ====================
class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
) {
    @Composable
    fun PageContent() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                fontSize = 15.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

// ==================== ONBOARDING PAGES LIST ====================
private val onboardingPages = listOf(
    OnboardingPage(
        title = "Easy Time Management",
        description = "With management based on priority and daily tasks, it will give you convenience in managing and determining the tasks that must be done first.",
        imageRes = R.drawable.first
    ),
    OnboardingPage(
        title = "Increase Work Effectiveness",
        description = "Time management and the determination of more important tasks will give your job statistics better and always improve.",
        imageRes = R.drawable.second
    ),
    OnboardingPage(
        title = "Reminder Notification",
        description = "This app also provides reminders for you so you don't forget your tasks and complete them on time.",
        imageRes = R.drawable.third
    )
)

// ==================== ONBOARDING SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onGetStarted: () -> Unit = {}) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val currentPage = onboardingPages[page]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // === Top row: Indicator + Skip ===
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        repeat(onboardingPages.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(if (index == page) 10.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == page)
                                            Color(0xFF007AFF)
                                        else
                                            Color(0xFFE5E5E5)
                                    )
                            )
                            if (index < onboardingPages.lastIndex)
                                Spacer(modifier = Modifier.width(6.dp))
                        }
                    }

                    TextButton(
                        onClick = { scope.launch { pagerState.animateScrollToPage(onboardingPages.lastIndex) } }
                    ) {
                        Text("Skip", color = Color(0xFF007AFF), fontSize = 15.sp)
                    }
                }

                // === Center content: call class PageContent() ===
                Box(modifier = Modifier.weight(1f)) {
                    currentPage.PageContent()
                }

                // === Bottom row: Back + Next / Get Started ===
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (page > 0) {
                        IconButton(
                            onClick = { scope.launch { pagerState.animateScrollToPage(page - 1) } },
                            modifier = Modifier.size(52.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF007AFF)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(52.dp))
                    }

                    Button(
                        onClick = {
                            if (page == onboardingPages.lastIndex) onGetStarted()
                            else scope.launch { pagerState.animateScrollToPage(page + 1) }
                        },
                        modifier = Modifier
                            .width(230.dp)
                            .height(52.dp)
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                    ) {
                        Text(
                            text = if (page == onboardingPages.lastIndex) "Get Started" else "Next",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(52.dp))
                }
            }
        }
    }
}

// ==================== PREVIEW ====================
@Preview(showBackground = true, name = "Preview Onboarding")
@Composable
fun OnboardingPreview() {
    MaterialTheme { OnboardingScreen() }
}