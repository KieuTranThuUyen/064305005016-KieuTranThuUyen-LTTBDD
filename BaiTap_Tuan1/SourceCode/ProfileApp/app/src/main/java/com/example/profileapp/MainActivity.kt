package com.example.profileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profileapp.ui.theme.ProfileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        ProfileTopBar(
                            onBackClick = { finish() }, // Quay lại màn hình trước
                            onEditClick = { /* Chưa làm chức năng chỉnh sửa */ }
                        )
                    }
                ) { innerPadding ->
                    ProfileScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    TopAppBar(
        title = { }, // trống như hình
        navigationIcon = {
            OutlinedIconButton(
                onClick = onBackClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Quay lại"
                )
            }
        },
        actions = {
            OutlinedIconButton(
                onClick = onEditClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Chỉnh sửa",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.ic_avatar),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Tên
        Text(
            text = "Kiều Trần Thu Uyên",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lớp & trường
        Text(
            text = "CN2303C, UTH",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileAppTheme {
        Scaffold(
            topBar = {
                ProfileTopBar(onBackClick = {}, onEditClick = {})
            }
        ) {
            ProfileScreen(modifier = Modifier.padding(it))
        }
    }
}
