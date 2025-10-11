package com.example.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
            MyFirstApp()
        }
    }
}

@Composable
fun MyFirstApp() {
    var showName by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "My First App",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showName) {
                Text(
                    text = "Hello",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            } else {
                Text(
                    text = "I'm",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = "Kieu Tran Thu Uyen",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }

        Button(
            onClick = { showName = true },
            modifier = Modifier
                .width(160.dp)
                .height(48.dp)
        ) {
            Text(text = "Say Hi!", fontSize = 18.sp)
        }
    }
}
