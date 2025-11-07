package com.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PermissionScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // DÙNG "permissions" ĐỂ TRÁNH LỖI
        scope.launch {
            if (pagerState.currentPage < 2) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                onFinish()
            }
        }
    }

    val pages = listOf(
        Triple(Icons.Filled.LocationOn, "Location", "Allow maps to access your location while you use the app?"),
        Triple(Icons.Filled.Notifications, "Notification", "Please enable notifications to receive updates and reminders"),
        Triple(Icons.Filled.CameraAlt, "Camera", "We need access to your camera to scan QR codes")
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        HorizontalPager(state = pagerState) { page ->
            val (icon, title, desc) = pages[page]
            val isLast = page == 2

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(100.dp))

                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F7FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color(0xFFFF6B35), Modifier.size(72.dp))
                }

                Spacer(Modifier.height(40.dp))
                Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text(desc, fontSize = 16.sp, color = Color.Gray, textAlign = TextAlign.Center)

                Spacer(Modifier.height(50.dp))

                Button(
                    onClick = {
                        val permission = when (page) {
                            0 -> Manifest.permission.ACCESS_FINE_LOCATION
                            1 -> if (Build.VERSION.SDK_INT >= 33) Manifest.permission.POST_NOTIFICATIONS else null
                            2 -> Manifest.permission.CAMERA
                            else -> null
                        }

                        if (permission == null || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            scope.launch { pagerState.animateScrollToPage(page + 1) }
                            return@Button
                        }

                        launcher.launch(arrayOf(permission))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B35)),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.width(220.dp).height(52.dp)
                ) {
                    Text(if (page == 0) "Allow" else "Turn on", color = Color.White, fontSize = 17.sp)
                }

                Spacer(Modifier.height(16.dp))
                TextButton(onClick = { scope.launch { pagerState.animateScrollToPage(page + 1) } }) {
                    Text("Skip for now", color = Color(0xFFFF6B35))
                }

                Spacer(Modifier.weight(1f))

                AnimatedVisibility(visible = isLast) {
                    Column {
                        Button(
                            onClick = onFinish,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Text("Login", color = Color.White)
                        }
                        Spacer(Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = onFinish,
                            border = BorderStroke(1.5.dp, Color(0xFFFF6B35)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF6B35)),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Text("Continue without account")
                        }
                    }
                }
                Spacer(Modifier.height(100.dp))
            }
        }

        // Dots + Skip
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(if (pagerState.currentPage == i) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (pagerState.currentPage == i) Color(0xFFFF6B35) else Color(0xFFFF6B35).copy(0.4f))
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onFinish) {
                Text("Skip", color = Color(0xFFFF6B35), fontSize = 16.sp)
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}