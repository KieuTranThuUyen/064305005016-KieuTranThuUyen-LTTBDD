package com.example.library

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.foundation.clickable

/*------------------ DỮ LIỆU ------------------*/
data class Book(val id: Int, val title: String, var selected: Boolean = false)
data class Student(val id: Int, val name: String, val borrowedBooks: MutableList<Book> = mutableListOf())

/*------------------ ACTIVITY CHÍNH ------------------*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LibraryApp(this)
            }
        }
    }
}

/*------------------ LƯU / TẢI DỮ LIỆU ------------------*/
fun saveStudentsToPrefs(context: Context, students: List<Student>) {
    val sharedPref = context.getSharedPreferences("library_prefs", Context.MODE_PRIVATE)
    val json = Gson().toJson(students)
    sharedPref.edit().apply {
        putString("students_data", json)
        apply()
    }
}

fun loadStudentsFromPrefs(context: Context): MutableList<Student>? {
    val sharedPref = context.getSharedPreferences("library_prefs", Context.MODE_PRIVATE)
    val json = sharedPref.getString("students_data", null)
    return if (json != null) {
        val type = object : TypeToken<MutableList<Student>>() {}.type
        Gson().fromJson(json, type)
    } else null
}

/*------------------ ỨNG DỤNG ------------------*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryApp(context: Context) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "HỆ THỐNG",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "QUẢN LÝ THƯ VIỆN",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "quanly",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("quanly") { LibraryUI(context) }
            composable("sach") { BookListScreen() }
            composable("sinhvien") { StudentListScreen(context) }
        }
    }
}

/*------------------ THANH ĐIỀU HƯỚNG ------------------*/
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("quanly", "sach", "sinhvien")
    val icons = listOf(Icons.Filled.Home, Icons.AutoMirrored.Filled.MenuBook, Icons.Filled.Person)
    val labels = listOf("Quản lý", "DS Sách", "Sinh viên")
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, route ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = { navController.navigate(route) },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = labels[index],
                        tint = if (currentRoute == route) Color(0xFF1565C0) else Color.Gray
                    )
                },
                label = {
                    Text(
                        labels[index],
                        color = if (currentRoute == route) Color(0xFF1565C0) else Color.Gray
                    )
                }
            )
        }
    }
}

/*------------------ GIAO DIỆN CHÍNH (QUẢN LÝ) ------------------*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryUI(context: Context) {
    val allBooks = remember {
        mutableStateListOf(
            Book(1, "Sách 01"),
            Book(2, "Sách 02"),
            Book(3, "Sách 03"),
            Book(4, "Sách 04"),
            Book(5, "Sách 05")
        )
    }

    val students = remember {
        mutableStateListOf(
            *(loadStudentsFromPrefs(context)
                ?: listOf(
                    Student(1, "Nguyen Van A", mutableListOf(allBooks[0], allBooks[1])),
                    Student(2, "Nguyen Thi B", mutableListOf(allBooks[2])),
                    Student(3, "Nguyen Van C", mutableListOf())
                )).toTypedArray()
        )
    }

    var selectedStudent by remember { mutableStateOf(students[0]) }
    var showStudentList by remember { mutableStateOf(false) } // kiểm soát danh sách sinh viên
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // chữ "Sinh viên" trên khung
        Text(
            "Sinh viên",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(4.dp))

        // Khung tên + nút thay đổi
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = selectedStudent.name,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { showStudentList = !showStudentList },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                modifier = Modifier
                    .height(IntrinsicSize.Min)  // 🟢 Đồng bộ chiều cao với TextField
                    .wrapContentWidth(),
                shape = RoundedCornerShape(8.dp)

            ) {
                Text("Thay đổi", color = Color.White)
            }
        }

        // Hiển thị danh sách sinh viên khi nhấn nút
        if (showStudentList) {
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                    .padding(4.dp)
                    .heightIn(max = 200.dp) // giới hạn chiều cao
            ) {
                items(students) { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedStudent = student
                                showStudentList = false
                            }
                            .padding(8.dp)
                    ) {
                        Text(student.name, fontSize = 16.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Phần danh sách sách đã mượn
        Text(
            "Danh sách sách đã mượn",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            if (selectedStudent.borrowedBooks.isEmpty()) {
                Text(
                    "Chưa mượn sách nào\nNhấn 'Thêm' để chọn sách muốn mượn.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            } else {
                LazyColumn {
                    items(selectedStudent.borrowedBooks) { book ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(checked = true, onCheckedChange = {})
                                Text(book.title, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { showAddDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Thêm", color = Color.White)
        }
    }

    // Dialog thêm sách
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {
                Button(onClick = {
                    val selected = allBooks.filter { it.selected }
                    selected.forEach { book ->
                        if (!selectedStudent.borrowedBooks.any { it.id == book.id }) {
                            selectedStudent.borrowedBooks.add(book.copy(selected = false))
                        }
                    }
                    saveStudentsToPrefs(context, students)
                    selectedStudent = selectedStudent.copy(
                        borrowedBooks = selectedStudent.borrowedBooks.toMutableList()
                    )
                    showAddDialog = false
                }) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) {
                    Text("Hủy")
                }
            },
            title = { Text("Chọn sách muốn mượn") },
            text = {
                LazyColumn {
                    items(allBooks) { book ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Checkbox(
                                checked = book.selected,
                                onCheckedChange = { checked -> book.selected = checked }
                            )
                            Text(book.title)
                        }
                    }
                }
            }
        )
    }
}



/*------------------ MÀN HÌNH DANH SÁCH SÁCH ------------------*/
@Composable
fun BookListScreen() {
    val books = listOf("Sách 01", "Sách 02", "Sách 03", "Sách 04", "Sách 05")
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Danh sách Sách", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(books) { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Text(book, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

/*------------------ MÀN HÌNH DANH SÁCH SINH VIÊN ------------------*/
@Composable
fun StudentListScreen(context: Context) {
    val students = loadStudentsFromPrefs(context)
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Danh sách Sinh viên", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(students ?: emptyList()) { s ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Text("${s.name} - ${s.borrowedBooks.size} sách", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

/*------------------ PREVIEW ------------------*/
@Preview(showBackground = true)
@Composable
fun LibraryPreview() {
    MaterialTheme {
        LibraryApp(context = androidx.compose.ui.platform.LocalContext.current)
    }
}
