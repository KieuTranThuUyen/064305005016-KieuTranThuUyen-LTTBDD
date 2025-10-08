package com.example.numberinputapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NumberInputApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberInputApp() {
    var inputValue by remember { mutableStateOf("") }
    var numbers by remember { mutableStateOf<List<Int>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Thực hành 02",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputValue,
                onValueChange = {
                    inputValue = it
                    errorMessage = ""
                    numbers = emptyList()
                },
                placeholder = { Text("Nhập vào số lượng") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFBDBDBD),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    try {
                        val n = inputValue.toInt()
                        if (n <= 0) {
                            errorMessage = "Dữ liệu bạn nhập không hợp lệ"
                            numbers = emptyList()
                        } else {
                            errorMessage = ""
                            numbers = (1..n).toList()
                        }
                    } catch (e: Exception) {
                        errorMessage = "Dữ liệu bạn nhập không hợp lệ"
                        numbers = emptyList()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text("Tạo", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            numbers.forEach { number ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .height(50.dp)
                        .background(Color.Red, shape = RoundedCornerShape(25.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
