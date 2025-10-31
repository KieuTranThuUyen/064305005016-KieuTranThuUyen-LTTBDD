package com.example.uthsmarttasks

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val channelId = "UTH_CHANNEL"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        createNotificationChannel()

        setContent {
            UTHSmartTasksTheme {
                // Truyền hàm signInWithGoogle vào
                LoginFlowScreen(
                    onSignInWithGoogle = { idToken ->
                        signInWithGoogle(idToken)
                    }
                )
            }
        }

        // Lấy FCM Token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("FCM Token: ${task.result}")
            }
        }
    }

    // HÀM ĐƯỢC GỌI TỪ LOGIN SCREEN
    private fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showNotification("Đăng nhập thành công!", "Xin chào ${user?.displayName}")
                } else {
                    showNotification("Lỗi đăng nhập", task.exception?.message ?: "Không xác định")
                }
            }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "UTH SmartTasks",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Thông báo đăng nhập và nhiệm vụ"
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title ?: "UTH SmartTasks")
            .setContentText(body ?: "Bạn có thông báo mới!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        getSystemService(NotificationManager::class.java).notify(notificationId, notification)
    }
}

// ==================== COMPOSE SCREENS ====================

@Composable
fun LoginFlowScreen(
    onSignInWithGoogle: (String) -> Unit
) {
    var screen by remember { mutableStateOf("login") }
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var fcmToken by remember { mutableStateOf("Nhấn để lấy FCM token") }
    var status by remember { mutableStateOf("Sẵn sàng") }
    val scope = rememberCoroutineScope()

    when (screen) {
        "login" -> LoginScreen(
            onSignInSuccess = { name, email ->
                userName = name
                userEmail = email
                screen = "profile"
            },
            onSignInWithGoogle = onSignInWithGoogle, // Truyền xuống
            onGetToken = {
                scope.launch {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        fcmToken = if (task.isSuccessful) task.result else "Lỗi lấy token"
                        status = "Token đã lấy!"
                    }
                }
            },
            token = fcmToken,
            status = status
        )
        "profile" -> UserProfileScreen(
            name = userName,
            email = userEmail,
            onBack = { screen = "login" }
        )
    }
}

@Composable
fun LoginScreen(
    onSignInSuccess: (String, String) -> Unit,
    onSignInWithGoogle: (String) -> Unit,
    onGetToken: () -> Unit,
    token: String,
    status: String
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFF1976D2)),
            contentAlignment = Alignment.Center
        ) {
            Text("UTH", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))
        Text("SmartTasks", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Text("A simple and efficient to-do app", fontSize = 14.sp, color = Color(0xFF777777))
        Spacer(Modifier.height(32.dp))

        Text("Welcome", fontSize = 20.sp, fontWeight = FontWeight.Medium)
        Text("Ready to explore? Log in to get started.", color = Color.Gray)
        Spacer(Modifier.height(32.dp))

        // GỌI HÀM signInWithGoogle KHI NHẤN
        Button(
            onClick = {
                onSignInSuccess("Melissa Peters", "melpeters@gmail.com")
                onSignInWithGoogle("fake-google-id-token-123") // Gọi thật
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("G", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text("SIGN IN WITH GOOGLE", color = Color.White)
            }
        }

        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = onGetToken, modifier = Modifier.fillMaxWidth()) {
            Text("LẤY FCM TOKEN")
        }

        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Text(token, fontSize = 10.sp, modifier = Modifier.padding(12.dp), lineHeight = 14.sp)
        }
        Text(status, color = Color.Green, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun UserProfileScreen(name: String, email: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Text(name.first().toString(), color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))
        Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Spacer(Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Column(Modifier.padding(16.dp)) {
                ProfileRow("Name", name)
                ProfileRow("Email", email)
                ProfileRow("Date of Birth", "23/05/1995")
            }
        }

        Spacer(Modifier.height(32.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back", color = Color.White)
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, fontWeight = FontWeight.Medium, modifier = Modifier.width(100.dp))
        Text(": $value", color = Color(0xFF333333))
    }
}

// ==================== PREVIEW ====================

@Preview(showBackground = true, name = "Login + Profile Flow")
@Composable
fun LoginFlowPreview() {
    UTHSmartTasksTheme {
        LoginFlowScreen(onSignInWithGoogle = {})
    }
}

// ==================== THEME ====================

@Composable
fun UTHSmartTasksTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF1976D2),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFF8F9FA)
        ),
        content = content
    )
}