package com.example.uthsmarttasks

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val channelId = "UTH_CHANNEL"
    private val notificationId = 1

    // Launcher cho Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            showNotification("Lỗi đăng nhập", "Google Sign-In thất bại")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        createNotificationChannel()

        // Cấu hình Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID") // Lấy từ Firebase Console
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            UTHSmartTasksTheme {
                LoginFlowScreen(
                    onGoogleSignInClick = {
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showNotification(
                        "Đăng nhập thành công!",
                        "Xin chào ${user?.displayName}"
                    )
                } else {
                    showNotification("Lỗi đăng nhập", task.exception?.message)
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
                description = "Thông báo đăng nhập"
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
            .setContentText(body ?: "Thông báo từ hệ thống")
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
    onGoogleSignInClick: () -> Unit
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
            onGoogleSignInClick = onGoogleSignInClick,
            onGetToken = {
                scope.launch {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        fcmToken = if (task.isSuccessful) task.result else "Lỗi"
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
    onGoogleSignInClick: () -> Unit,
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

        Button(
            onClick = onGoogleSignInClick,
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

// ... (UserProfileScreen, ProfileRow, Preview, Theme giữ nguyên như cũ)