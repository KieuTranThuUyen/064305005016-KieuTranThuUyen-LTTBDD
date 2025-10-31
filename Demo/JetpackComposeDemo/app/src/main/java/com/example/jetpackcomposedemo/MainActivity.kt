package com.example.jetpackcomposedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposedemo.ui.theme.JetpackComposeDemoTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeDemoTheme {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("list") { ListScreen(navController) }
        composable("counter") { CounterScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("progress") { ProgressScreen(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    showBack: Boolean = false,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF4A90E2),
                    textAlign = TextAlign.Center
                )
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF4A90E2)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = Modifier.shadow(4.dp)
    )
}

@Composable
fun StartScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Demo",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A90E2)
            )

            Button(
                onClick = { navController.navigate("list") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp)
                    .shadow(8.dp, RoundedCornerShape(50))
            ) {
                Text("Bắt đầu", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController) {
    val demos = listOf(
        "Counter App", "Settings Screen", "Profile Page",
        "Business Dashboard", "Progress Demo"
    )
    val routes = listOf("counter", "settings", "profile", "dashboard", "progress")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Demo Features",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF4A90E2)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                modifier = Modifier.shadow(4.dp)
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(demos.size) { index ->
                Card(
                    onClick = { navController.navigate(routes[index]) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.shadow(2.dp, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(demos[index], fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E1E1E))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Nhấn để mở ${demos[index]}", fontSize = 14.sp, color = Color.Gray)
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, "Go", tint = Color(0xFF4A90E2))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    ListScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterScreen(navController: NavHostController) {
    var counter by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { AppHeader("Counter Demo", showBack = true) { navController.popBackStack() } },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)

        ) {
            Text("Counter", fontSize = 44.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(counter.toString(), fontSize = 54.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { counter++ }) {
                    Text("+", fontSize = 44.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                }
                Button(onClick = { if (counter > 0) counter-- }) {
                    Text("-", fontSize = 44.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    CounterScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = { AppHeader("Settings", showBack = true) { navController.popBackStack() } },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Profile Image", fontSize = 18.sp)
                Image(
                    modifier = Modifier
                        .height(34.dp)
                        .clickable { },
                    painter = painterResource(id = R.drawable.sunflower),
                    contentDescription = "Profile Image"
                )
            }

            var isConsentChecked by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Accept non-essential cookies?", fontSize = 18.sp)
                Checkbox(checked = isConsentChecked, onCheckedChange = { isConsentChecked = it })
            }

            var isMobileDataChecked by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Download using mobile data?", fontSize = 18.sp)
                Switch(checked = isMobileDataChecked, onCheckedChange = { isMobileDataChecked = it })
            }

            var sliderValue by remember { mutableFloatStateOf(0f) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Text size:", fontSize = 18.sp)
                Slider(value = sliderValue, onValueChange = { sliderValue = it }, steps = 2)
            }

            var selectedPayment by remember { mutableStateOf("PayPal") }
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text("Preferred payment method", modifier = Modifier.padding(bottom = 8.dp))
                listOf("PayPal", "Credit Card", "Bank Transfer").forEach { method ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                        RadioButton(selected = (selectedPayment == method), onClick = { selectedPayment = method })
                        Text(method, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            var name by remember { mutableStateOf("") }
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            var showDialog by remember { mutableStateOf(false) }
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) { Text("Sign out") }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Sign out") },
                    text = { Text("Are you sure?") },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    var profileImage by remember { mutableStateOf<Any?>(R.drawable.avatar) }

    Scaffold(
        topBar = { AppHeader("Profile", showBack = true) { navController.popBackStack() } },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(modifier = Modifier.size(130.dp), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = profileImage as? Int ?: R.drawable.avatar),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(alpha = 0.3f))
                                .clickable { }
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                                .border(1.dp, Color(0xFF4A90E2), RoundedCornerShape(8.dp))
                                .clickable { }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Edit", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Jane Doe", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Mobile Developer | Tech Enthusiast", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 8.dp)) {
                        Button(onClick = { }, modifier = Modifier.weight(1f)) { Text("Follow") }
                        Button(onClick = { }, modifier = Modifier.weight(1f)) { Text("Message") }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}

data class DashboardItem(val title: String, val value: String, val color: Color, val description: String)

val dashboardItems = listOf(
    DashboardItem("Revenue", "$12.5K", Color(0xFF81C784), "Revenue increased 12% compared to last month."),
    DashboardItem("Expenses", "$5.2K", Color(0xFF64B5F6), "Expenses are stable compared to previous months."),
    DashboardItem("Profit", "$7.3K", Color(0xFFFFF176), "Profit margins remain consistent across departments."),
    DashboardItem("Customers", "1.2K", Color(0xFFE57373), "Customer base grew 8% this quarter.")
)

@Composable
fun DashboardScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(dashboardItems.first()) }

    Scaffold(
        topBar = { AppHeader("Business Dashboard", showBack = true) { navController.popBackStack() } },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    dashboardItems.take(2).forEach { item ->
                        DashboardCard(item = item, modifier = Modifier.weight(1f)) { selectedItem = it }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    dashboardItems.takeLast(2).forEach { item ->
                        DashboardCard(item = item, modifier = Modifier.weight(1f)) { selectedItem = it }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Details", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                    Text(selectedItem.title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(selectedItem.description, fontSize = 16.sp, color = Color.Gray, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun DashboardCard(item: DashboardItem, modifier: Modifier = Modifier, onItemClick: (DashboardItem) -> Unit) {
    Card(
        modifier = modifier.padding(8.dp).clickable { onItemClick(item) },
        colors = CardDefaults.cardColors(containerColor = item.color),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(item.title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.value, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(navController: NavHostController) {
    var progress by remember { mutableFloatStateOf(0.0f) }
    var autoProgress by remember { mutableFloatStateOf(0.0f) }

    LaunchedEffect(Unit) {
        while (true) {
            autoProgress = (autoProgress + 0.01f).coerceAtMost(1f)
            if (autoProgress >= 1f) autoProgress = 0f
            delay(100)
        }
    }

    Scaffold(
        topBar = { AppHeader("Progress Indicators", showBack = true) { navController.popBackStack() } },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Linear Progress (Manual)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())

            Text("Circular Progress (Manual)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            CircularProgressIndicator(progress = { progress })

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                progress = (progress + 0.1f).coerceAtMost(1f)
            }) {
                Text("Tăng tiến độ (${(progress * 100).toInt()}%)")
            }

            Spacer(modifier = Modifier.height(36.dp))
            Text("Auto Progress", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(progress = { autoProgress }, modifier = Modifier.fillMaxWidth())
            CircularProgressIndicator(progress = { autoProgress })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    ProgressScreen(navController = rememberNavController())
}