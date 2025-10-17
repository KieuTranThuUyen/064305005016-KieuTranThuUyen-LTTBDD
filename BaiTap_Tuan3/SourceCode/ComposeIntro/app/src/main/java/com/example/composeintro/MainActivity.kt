package com.example.composeintro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import com.example.composeintro.ui.theme.ComposeIntroTheme
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import java.util.Locale
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.IconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeIntroTheme {
                JetpackApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetpackApp() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        NavHost(navController, startDestination = "intro") {
            composable("intro") { IntroScreen(navController) }
            composable("list") { UIComponentListScreen(navController) }
            composable("text_detail") { TextDetailScreen(navController) }
            composable("image") { ImageScreen(navController) }
            composable("textfield") { TextFieldScreen(navController) }
            composable("row_layout") { RowLayoutScreen(navController) }
            composable("column_layout") { ColumnLayoutScreen(navController) }
            composable("passwordfield") { PasswordFieldScreen(navController) }
            composable("checkbox") { CheckboxScreen(navController) }
            composable("switch") { SwitchScreen(navController) }
            composable("card") { CardScreen(navController) }
            composable("radiobutton") { RadioButtonScreen(navController) }
            composable("button") { ButtonScreen(navController) }
            composable("slider") { SliderScreen(navController) }
            composable("listview") { ListViewScreen(navController) }
            composable("gridview") { GridViewScreen(navController) }
            composable("progressbar") { ProgressBarScreen(navController) }
            composable("box") { BoxScreen(navController) }
            composable("spacer") { SpacerScreen(navController) }
            composable("iconbutton") { IconButtonScreen(navController) }
        }
    }
}

@Composable
fun IntroScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("Kiều Trần Thu Uyên", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("MSSV: 064305005016", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.compose_logo),
                    contentDescription = "Logo Jetpack Compose",
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "Jetpack Compose",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("I'm ready", fontSize = 18.sp, color = Color.White)
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome to Jetpack Compose!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "You are now ready to explore the UI components.",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                showDialog = false
                                navController.navigate("list")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                        ) {
                            Text("OK", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UIComponentListScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "UI Components List", showBack = false) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val uiGroups = listOf(
                "Display" to listOf(
                    "Text" to ("Displays text" to "text_detail"),
                    "Image" to ("Displays an image" to "image"),
                    "ListView" to ("Scrollable list" to "listview"),
                    "GridView" to ("Grid layout" to "gridview"),
                    "ProgressBar" to ("Progress indicator" to "progressbar"),
                    "Card" to ("Displays content in a card" to "card")
                ),
                "Input" to listOf(
                    "TextField" to ("Input field for text" to "textfield"),
                    "PasswordField" to ("Input field for passwords" to "passwordfield"),
                    "Slider" to ("Sliding value selector" to "slider")
                ),
                "Selection" to listOf(
                    "Checkbox" to ("Toggleable check option" to "checkbox"),
                    "Switch" to ("On/off toggle switch" to "switch"),
                    "RadioButton" to ("Single selection option" to "radiobutton")
                ),
                "Layout" to listOf(
                    "Column" to ("Arranges elements vertically" to "column_layout"),
                    "Row" to ("Arranges elements horizontally" to "row_layout"),
                    "Box" to ("Overlays & positions content" to "box"),
                    "Spacer" to ("Creates space between" to "spacer")
                ),
                "Actions" to listOf(
                    "Button" to ("Action button" to "button"),
                    "IconButton" to ("Button with icon" to "iconbutton")
                )
            )

            uiGroups.forEach { (groupName, components) ->
                Text(
                    text = groupName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                components.forEach { (label, pair) ->
                    val (desc, route) = pair
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                            .clickable { navController.navigate(route) }
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = label,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = desc,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8BBD0), RoundedCornerShape(8.dp))
                    .clickable { }
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Tự tìm hiểu",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Tìm ra tất cả các thành phần UI Cơ bản",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDetailScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "Text Detail", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("The ")
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough, color = Color.Black)) {
                            append("quick")
                        }
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFB87333))) { append("B") }
                        withStyle(SpanStyle(color = Color(0xFFB87333))) { append("rown") }
                        append("\n")
                        withStyle(SpanStyle(color = Color.Black)) { append("fox ") }
                        withStyle(SpanStyle(letterSpacing = 8.sp, color = Color.Black)) { append("jumps ") }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) { append("over") }
                        append("\n")
                        withStyle(SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Black)) { append("the ") }
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color.Black)) { append("lazy") }
                        append(" dog.")
                    },
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "Images", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ut_hcm_building),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "https://giaothongvantaitphcm.edu.vn/wp-content/uploads/2025/01/Logo-GTVT.png",
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                modifier = Modifier.fillMaxWidth()
            )

            Image(
                painter = painterResource(id = R.drawable.ut_hcm_campus),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "In app",
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "TextField", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var text by remember { mutableStateOf("") }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Thông tin nhập") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = text.ifEmpty { "Tự động cập nhật dữ liệu theo textfield" },
                color = Color.Red,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "PasswordField", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var password by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nhập mật khẩu") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckboxScreen(navController: NavController) {
    val options = listOf("Đọc sách", "Nghe nhạc", "Xem phim", "Chơi thể thao")
    val checkedStates = remember { mutableStateListOf(false, false, false, false) }

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Checkbox",
                showBack = true
            ) { navController.popBackStack() }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "CHECKBOX – Chọn nhiều sở thích",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    options.forEachIndexed { index, option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { checkedStates[index] = !checkedStates[index] }
                                .background(
                                    if (checkedStates[index]) Color(0xFFE3F2FD)
                                    else Color.Transparent
                                )
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkedStates[index],
                                onCheckedChange = { checkedStates[index] = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF1976D2),
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(
                                text = option,
                                fontSize = 18.sp,
                                color = Color(0xFF222222),
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val selectedItems = options.filterIndexed { index, _ -> checkedStates[index] }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = if (selectedItems.isEmpty())
                        "Chưa chọn mục nào"
                    else
                        "Đã chọn: ${selectedItems.joinToString(", ")}",
                    fontSize = 16.sp,
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Switch",
                showBack = true
            ) { navController.popBackStack() }
        },
        containerColor = Color.White
    ) { padding ->

        var isOn by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "SWITCH - Bật / Tắt trạng thái",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isOn) "Đang bật" else "Đang tắt",
                        fontSize = 18.sp,
                        color = Color(0xFF333333)
                    )
                    Switch(
                        checked = isOn,
                        onCheckedChange = { isOn = it }
                    )
                }
            }

            Text(
                text = "Trạng thái hiện tại: ${if (isOn) "Bật" else "Tắt"}",
                fontSize = 16.sp,
                color = Color(0xFF333333),
                fontWeight = FontWeight.Medium
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Card",
                showBack = true
            ) { navController.popBackStack() }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.compose_logo),
                        contentDescription = "Compose Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Jetpack Compose Card",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Jetpack Compose makes building Android UI easier and faster. This card demonstrates how to display an image, text, and a button together.",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { /* TODO: Handle click */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Learn More", color = Color.White)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowLayoutScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "Row Layout", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F7F7), RoundedCornerShape(16.dp))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { index ->
                            val color = if (index == 1) Color(0xFF448AFF) else Color(0xFF90CAF9)
                            Box(
                                modifier = Modifier
                                    .size(width = 80.dp, height = 50.dp)
                                    .background(color, RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnLayoutScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "Column Layout", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(3) { index ->
                        val color = when (index) {
                            1 -> Color(0xFF66BB6A)
                            else -> Color(0xFFA5D6A7)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .background(color, RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioButtonScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "RadioButton", showBack = true) { navController.popBackStack() } },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Chọn giới tính:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    var selectedGender by remember { mutableStateOf("male") }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedGender == "male",
                                onClick = { selectedGender = "male" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2196F3))
                            )
                            Text("Nam", fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedGender == "female",
                                onClick = { selectedGender = "female" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFE91E63))
                            )
                            Text("Nữ", fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    }

                    Text(
                        text = "Đã chọn: ${if (selectedGender == "male") "Nam" else "Nữ"}",
                        fontSize = 14.sp,
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Chọn màu yêu thích:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    var selectedColor by remember { mutableStateOf("blue") }

                    val colorOptions = listOf(
                        Triple("Xanh dương", Color(0xFF2196F3), "blue"),
                        Triple("Đỏ", Color(0xFFF44336), "red"),
                        Triple("Xanh lá", Color(0xFF4CAF50), "green")
                    )

                    colorOptions.forEach { (label, color, value) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedColor == value,
                                onClick = { selectedColor = value },
                                colors = RadioButtonDefaults.colors(selectedColor = color)
                            )
                            Text(label, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    }

                    Text(
                        text = "Màu yêu thích: $selectedColor",
                        fontSize = 14.sp,
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "Button", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->

        var clickCount by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Căn giữa theo chiều dọc
            horizontalAlignment = Alignment.CenterHorizontally // Căn giữa theo chiều ngang
        ) {

            Button(
                onClick = { clickCount++ },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Click Me!", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Clicked: $clickCount times", color = Color.Red, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { clickCount = 0 },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575)),
                shape = RoundedCornerShape(12.dp),
                enabled = clickCount > 0
            ) {
                Text("Reset", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderScreen(navController: NavController) {
    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Slider",
                showBack = true
            ) { navController.popBackStack() }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var sliderValue by remember { mutableFloatStateOf(0f) }

            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF007BFF),
                    activeTrackColor = Color(0xFF007BFF)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Value: ${sliderValue.toInt()}%",
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListViewScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "ListView", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(20) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Item $index",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )
                            Text(
                                "Description for item $index",
                                color = Color(0xFF1565C0)
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridViewScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "GridView", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(12) { index ->
                val backgroundColor = when (index % 4) {
                    0 -> Color(0xFF2196F3)
                    1 -> Color(0xFFF44336)
                    2 -> Color(0xFF4CAF50)
                    else -> Color(0xFFFFC107)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Item $index",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBarScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "ProgressBar", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF007BFF),
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            var progress by remember { mutableFloatStateOf(0f) }
            LaunchedEffect(Unit) {
                while (true) {
                    delay(50)
                    progress = (progress + 0.01f).coerceAtMost(1f)
                    if (progress >= 1f) progress = 0f
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(8.dp),
                color = Color(0xFF007BFF),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )

            Text(
                text = "Progress: ${String.format(Locale.getDefault(), "%.1f", progress * 100)}%",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScreen(navController: NavController) {
    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Box Layout",
                showBack = true
            ) { navController.popBackStack() }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "BOX - Chồng lớp & Căn chỉnh",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Card(
                modifier = Modifier.size(220.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(50.dp)
                            .background(Color(0xFF4CAF50), RoundedCornerShape(10.dp))
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(90.dp)
                            .background(Color(0xFFFF9800), RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = "CENTER",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .background(Color(0xFFE91E63), RoundedCornerShape(10.dp))
                    )
                }
            }

            Text(
                text = "TopStart • Center • BottomEnd",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                fontStyle = FontStyle.Italic
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(vertical = 8.dp),
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )

            Text(
                text = "Các kiểu căn chỉnh",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1976D2)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF2196F3), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text("TL", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF4CAF50), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("C", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFF44336), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text("BR", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacerScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "Spacer", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "SPACER - Tạo khoảng cách",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF2196F3), RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFF44336), RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                )
            }

            Text(
                "Spacer thủ công: height(8.dp), height(16.dp)",
                fontSize = 14.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Arrangement.spacedBy():",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFFFF9800), RoundedCornerShape(10.dp))
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF9C27B0), RoundedCornerShape(10.dp))
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF607D8B), RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Row spacedBy():",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE91E63), RoundedCornerShape(8.dp))
                )
                Text("16dp")
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF00BCD4), RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonScreen(navController: NavController) {
    Scaffold(
        topBar = { ScreenHeader(title = "IconButton", showBack = true) { navController.popBackStack() } },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "ICONBUTTON - Nút có icon",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            var likes by remember { mutableIntStateOf(0) }
            var isFavorite by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { likes++ }) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = Color(0xFFE91E63)
                            )
                        }
                        Text("$likes likes", fontSize = 16.sp)
                    }
                    Text("Like Button", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { isFavorite = !isFavorite }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color(0xFFFF9800) else Color.Gray
                            )
                        }
                        Text(
                            if (isFavorite) "Favorited ❤️" else "Favorite",
                            fontSize = 16.sp
                        )
                    }
                    Text("Toggle Favorite", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "More Icon Buttons",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Share",
                                tint = Color(0xFF2196F3)
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFF44336)
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Outlined.Menu,
                                contentDescription = "Menu",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    }

                    Text("Các icon thường gặp trong ứng dụng", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHeader(title: String, showBack: Boolean = false, onBackClick: (() -> Unit)? = null) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            if (showBack && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF1976D2),
            navigationIconContentColor = Color(0xFF1976D2)
        )
    )
}
