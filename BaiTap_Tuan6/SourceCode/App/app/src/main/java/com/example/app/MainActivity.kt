package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PermissionScreen(
                        onFinish = {
                            setContent {
                                androidx.compose.material3.Text(
                                    text = "XONG! Bạn đã cấp quyền thành công!",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.fillMaxSize().wrapContentSize()
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}