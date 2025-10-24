package com.example.smarttasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTasksApp()
        }
    }
}

// Data class lưu thông tin người dùng
data class UserData(
    var email: String = "",
    var verificationCode: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)

@Composable
fun SmartTasksApp() {
    val navController = rememberNavController()
    val userData = remember { UserData() }

    NavHost(navController, startDestination = "forgetPassword") {
        composable("forgetPassword") { ForgetPasswordScreen(navController, userData) }
        composable("verification") { VerificationScreen(navController, userData) }
        composable("resetPassword") { ResetPasswordScreen(navController, userData) }
        composable("confirm") { ConfirmScreen(navController, userData) }
    }
}

// ---------------- ForgetPasswordScreen ----------------
@Composable
fun ForgetPasswordScreen(navController: NavHostController, userData: UserData) {
    var email by remember { mutableStateOf(userData.email) }

    val dateFormat = SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.getDefault())
    val currentTime = dateFormat.format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.uth),
                contentDescription = "UTH Logo",
                modifier = Modifier.size(100.dp)
            )
            Text(
                "SmartTasks",
                fontSize = 20.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Forget Password?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Enter your Email, we will send you a verification code", fontSize = 14.sp, color = Color.Gray)

            // Email có thể chỉnh sửa
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Your Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị thông tin đã xác nhận từ ConfirmScreen, không chỉnh sửa được
            if (userData.verificationCode.isNotEmpty() || userData.password.isNotEmpty() || userData.email.isNotEmpty()) {
                Text("Thông tin đã xác nhận:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                // Email xác nhận (không thể sửa)
                if (userData.email.isNotEmpty()) {
                    OutlinedTextField(
                        value = userData.email,
                        onValueChange = {},
                        label = { Text("Email đã xác nhận") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Confirmed Email Icon") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        enabled = false // Không chỉnh sửa được
                    )
                }

                // Verification Code
                if (userData.verificationCode.isNotEmpty()) {
                    OutlinedTextField(
                        value = userData.verificationCode,
                        onValueChange = {},
                        label = { Text("Verification Code") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Code Icon") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        enabled = false
                    )
                }

                // Password
                if (userData.password.isNotEmpty()) {
                    OutlinedTextField(
                        value = userData.password,
                        onValueChange = {},
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        enabled = false
                    )
                }
            }

            Text("Time: $currentTime (+07)", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))

            Button(
                onClick = {
                    userData.email = email
                    navController.navigate("verification")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(120.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Next", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}



// ---------------- VerificationScreen ----------------
@Composable
fun VerificationScreen(navController: NavHostController, userData: UserData) {
    val codeLength = 6
    val codeDigits = remember { mutableStateListOf(*Array(codeLength) { "" }) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF2196F3), RoundedCornerShape(50.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.uth),
                contentDescription = "UTH Logo",
                modifier = Modifier.size(100.dp)
            )
            Text(
                "SmartTasks",
                fontSize = 20.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Verify Code", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("We just sent you on your registered Email", fontSize = 14.sp, color = Color.Gray)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 0 until codeLength) {
                    OutlinedTextField(
                        value = codeDigits[i],
                        onValueChange = { value ->
                            if (value.length <= 1) {
                                codeDigits[i] = value
                                userData.verificationCode = codeDigits.joinToString("")
                                if (value.isNotEmpty() && i < codeLength - 1) {
                                    focusManager.moveFocus(FocusDirection.Next)
                                } else if (value.isEmpty() && i > 0) {
                                    focusManager.moveFocus(FocusDirection.Previous)
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Button(
                onClick = {
                    if (userData.verificationCode.length == codeLength) {
                        navController.navigate("resetPassword")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(120.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Next", color = Color.White)
            }
        }
    }
}

// ---------------- ResetPasswordScreen ----------------
@Composable
fun ResetPasswordScreen(navController: NavHostController, userData: UserData) {
    var password by remember { mutableStateOf(userData.password) }
    var confirmPassword by remember { mutableStateOf(userData.confirmPassword) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF2196F3), RoundedCornerShape(50.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.uth),
                contentDescription = "UTH Logo",
                modifier = Modifier.size(100.dp)
            )
            Text(
                "SmartTasks",
                fontSize = 20.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Create new password", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Your new password must be different from previously used password",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password Icon") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    userData.password = password
                    userData.confirmPassword = confirmPassword
                    navController.navigate("confirm")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(120.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Next", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

// ---------------- ConfirmScreen ----------------
@Composable
fun ConfirmScreen(navController: NavHostController, userData: UserData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF2196F3), RoundedCornerShape(50.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.uth),
                contentDescription = "UTH Logo",
                modifier = Modifier.size(100.dp)
            )
            Text(
                "SmartTasks",
                fontSize = 20.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Confirm", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("We are here to help you!", fontSize = 14.sp, color = Color.Gray)

            OutlinedTextField(
                value = userData.email,
                onValueChange = { },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = false
            )
            OutlinedTextField(
                value = userData.verificationCode,
                onValueChange = { },
                label = { Text("Verification Code") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Code Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = false
            )
            OutlinedTextField(
                value = userData.password,
                onValueChange = { },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = false
            )

            Button(
                onClick = {
                    navController.navigate("forgetPassword") {
                        popUpTo("forgetPassword") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(120.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Submit", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

// ---------------- Previews ----------------
@Preview(showBackground = true)
@Composable
fun ForgetPasswordPreview() = ForgetPasswordScreen(rememberNavController(), UserData())

@Preview(showBackground = true)
@Composable
fun VerificationPreview() = VerificationScreen(rememberNavController(), UserData())

@Preview(showBackground = true)
@Composable
fun ResetPasswordPreview() = ResetPasswordScreen(rememberNavController(), UserData())

@Preview(showBackground = true)
@Composable
fun ConfirmPreview() = ConfirmScreen(rememberNavController(), UserData())
