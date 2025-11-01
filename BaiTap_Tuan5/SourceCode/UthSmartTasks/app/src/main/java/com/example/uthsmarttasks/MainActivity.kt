package com.example.uthsmarttasks

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val channelId = "UTH_CHANNEL"
    private val notifLoginId = 1

    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        setContent {
            UTHSmartTasksTheme {
                var currentScreen by remember { mutableStateOf("login") }
                var showSmsForm by remember { mutableStateOf(false) }

                var userName by remember { mutableStateOf("") }
                var userEmail by remember { mutableStateOf("") }
                var userPhone by remember { mutableStateOf("") }
                var fcmToken by remember { mutableStateOf("Nhấn để lấy FCM token") }
                var status by remember { mutableStateOf("Sẵn sàng") }

                var phoneNumber by remember { mutableStateOf("") }
                var otpCode by remember { mutableStateOf("") }

                val coroutineScope = rememberCoroutineScope()

                val signInWithGoogle: () -> Unit = {
                    coroutineScope.launch {
                        try {
                            val clientId = getString(R.string.google_client_id)
                            val credentialManager =
                                androidx.credentials.CredentialManager.create(this@MainActivity)
                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setServerClientId(clientId)
                                .setFilterByAuthorizedAccounts(false)
                                .build()

                            val request = androidx.credentials.GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()

                            val result =
                                credentialManager.getCredential(this@MainActivity, request)
                            val credential = result.credential

                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)
                            val idToken = googleIdTokenCredential.idToken
                            val firebaseCredential =
                                GoogleAuthProvider.getCredential(idToken, null)

                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this@MainActivity) { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        userName = user?.displayName ?: "Người dùng"
                                        userEmail = user?.email ?: "Không có email"
                                        currentScreen = "profile"
                                        showNotification("Đăng nhập thành công!", "Xin chào $userName")
                                    } else {
                                        showNotification("Lỗi đăng nhập", task.exception?.message ?: "Lỗi")
                                    }
                                }
                        } catch (e: Exception) {
                            showNotification("Lỗi", e.message ?: "Không thể đăng nhập")
                        }
                    }
                }

                val sendOtp: (String) -> Unit = { rawNumber ->
                    val phoneNumberE164 = if (rawNumber.startsWith("0"))
                        "+84${rawNumber.drop(1)}"
                    else rawNumber

                    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    userName = "Uyên"
                                    userPhone = phoneNumber
                                    userEmail = ""
                                    currentScreen = "profile"
                                    showNotification("Đăng nhập OTP thành công!", "Xin chào $userName")
                                }
                            }
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            Toast.makeText(this@MainActivity, "Lỗi gửi OTP: ${e.message}", Toast.LENGTH_LONG).show()
                        }

                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            storedVerificationId = verificationId
                            resendToken = token
                            Toast.makeText(this@MainActivity, "Mã OTP đã gửi tới $phoneNumberE164", Toast.LENGTH_SHORT).show()
                        }
                    }

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumberE164)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }

                val verifyOtp: (String) -> Unit = { code ->
                    val verificationId = storedVerificationId
                    if (verificationId != null) {
                        val credential = PhoneAuthProvider.getCredential(verificationId, code)
                        auth.signInWithCredential(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                userName = "Uyên"
                                userPhone = phoneNumber
                                userEmail = ""
                                currentScreen = "profile"
                                showNotification("Đăng nhập OTP thành công!", "Xin chào $userName")
                            } else {
                                Toast.makeText(this@MainActivity, "OTP không chính xác!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Chưa gửi OTP!", Toast.LENGTH_SHORT).show()
                    }
                }

                val getFcmToken: () -> Unit = {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            fcmToken = task.result ?: "Không có token"
                            status = "Token đã lấy!"
                        } else {
                            fcmToken = "Lỗi lấy token"
                            status = "Lỗi: ${task.exception?.message}"
                        }
                    }
                }

                when (currentScreen) {
                    "login" -> LoginScreen(
                        onSignInWithGoogle = signInWithGoogle,
                        onShowSmsForm = { showSmsForm = true },
                        showSmsForm = showSmsForm,
                        onSendOtp = sendOtp,
                        onVerifyOtp = verifyOtp,
                        onGetToken = getFcmToken,
                        phoneNumber = phoneNumber,
                        onPhoneChange = { phoneNumber = it },
                        otpCode = otpCode,
                        onOtpChange = { otpCode = it },
                        token = fcmToken,
                        status = status
                    )

                    "profile" -> UserProfileScreen(
                        name = userName,
                        email = userEmail,
                        phone = userPhone,
                        onBack = { currentScreen = "login"; showSmsForm = false }
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "UTH SmartTasks",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Thông báo đăng nhập và FCM" }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title ?: "UTH SmartTasks")
            .setContentText(body ?: "Thông báo mới")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        getSystemService(NotificationManager::class.java).notify(notifLoginId, notification)
    }
}

// FCM service giữ nguyên
class MyFirebaseMessagingReceiver : FirebaseMessagingService() {
    private val channelId = "UTH_CHANNEL"
    private val notifFcmId = 2

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"]
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"]
        sendNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("FCM Token mới: $token")
    }

    private fun sendNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title ?: "UTH SmartTasks")
            .setContentText(body ?: "Thông báo mới")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        getSystemService(NotificationManager::class.java).notify(notifFcmId, notification)
    }
}

// Compose UI
@Composable
fun LoginScreen(
    onSignInWithGoogle: () -> Unit,
    onShowSmsForm: () -> Unit,
    showSmsForm: Boolean,
    onSendOtp: (String) -> Unit,
    onVerifyOtp: (String) -> Unit,
    onGetToken: () -> Unit,
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    token: String,
    status: String
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFF1976D2)),
            contentAlignment = Alignment.Center
        ) {
            Text("UTH", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        Text("SmartTasks", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onSignInWithGoogle,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
        ) { Text("ĐĂNG NHẬP GOOGLE", color = Color.White) }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onShowSmsForm,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
        ) { Text("ĐĂNG NHẬP SMS", color = Color.White) }

        if (showSmsForm) {
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneChange,
                label = { Text("Số điện thoại") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = { onSendOtp(phoneNumber) }, modifier = Modifier.fillMaxWidth()) {
                Text("Gửi OTP")
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = otpCode,
                onValueChange = onOtpChange,
                label = { Text("Nhập mã OTP") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = { onVerifyOtp(otpCode) }, modifier = Modifier.fillMaxWidth()) {
                Text("Xác nhận OTP")
            }
        }

        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = onGetToken, modifier = Modifier.fillMaxWidth()) {
            Text("LẤY FCM TOKEN")
        }
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    clipboardManager.setText(AnnotatedString(token))
                    copied = true
                },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Text(
                text = if (copied) "✅ Đã sao chép token!" else token,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
        Text(status, color = Color.Green, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun UserProfileScreen(name: String, email: String, phone: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Text(name.firstOrNull()?.toString() ?: "U", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Spacer(Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {
            Column(Modifier.padding(16.dp)) {
                ProfileRow("Name", name)
                if (phone.isNotEmpty()) ProfileRow("SĐT", phone)
                if (email.isNotEmpty()) ProfileRow("Email", email)
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
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(label, fontWeight = FontWeight.Medium, modifier = Modifier.width(100.dp))
        Text(": $value", color = Color(0xFF333333))
    }
}

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
