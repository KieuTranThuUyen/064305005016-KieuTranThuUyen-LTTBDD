package com.example.agecheckapp

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agecheckapp.ui.theme.AgeCheckAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgeCheckAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Practice01Screen()
                }
            }
        }
    }
}

@Composable
fun Practice01Screen() {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "THỰC HÀNH 01",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .background(Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                .width(280.dp)
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Họ và tên",
                        fontSize = 16.sp,
                        modifier = Modifier.width(80.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Tuổi",
                        fontSize = 16.sp,
                        modifier = Modifier.width(80.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                val input = age.trim()
                val ageValue = input.toIntOrNull()

                result = when {
                    name.isBlank() -> "Vui lòng nhập họ và tên."
                    input.isEmpty() -> "Vui lòng nhập tuổi."
                    ageValue == null -> "Tuổi phải là số hợp lệ."
                    else -> when {
                        ageValue > 65 -> "$name là Người già"
                        ageValue in 6..65 -> "$name là Người lớn"
                        ageValue in 2..5 -> "$name là Trẻ em"
                        ageValue < 2 -> "$name là Em bé"
                        else -> "Tuổi không hợp lệ"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
            modifier = Modifier
                .width(120.dp)
                .height(45.dp)
        ) {
            Text(
                text = "Kiểm tra",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (result.isNotEmpty()) {
            Text(
                text = result,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
