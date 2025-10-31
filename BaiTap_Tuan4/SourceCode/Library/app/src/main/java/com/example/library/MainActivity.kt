package com.example.library

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

/*------------------ OOP MODELS ------------------*/
open class Person(val name: String)

data class Book(val id: Int, val title: String, var selected: Boolean = false)

class Student(
    name: String,
    val borrowedBooks: MutableList<Book> = mutableListOf()
) : Person(name) {
    fun borrowBooks(books: List<Book>) {
        books.forEach { book ->
            if (!borrowedBooks.any { it.id == book.id }) borrowedBooks.add(book.copy(selected = false))
        }
    }
}

/*------------------ REPOSITORY ------------------*/
interface StudentRepository {
    fun save(students: List<Student>)
    fun load(): MutableList<Student>
}

class SharedPrefStudentRepository(private val context: Context) : StudentRepository {
    override fun save(students: List<Student>) {
        val sharedPref = context.getSharedPreferences("library_prefs", Context.MODE_PRIVATE)
        val json = Gson().toJson(students)
        sharedPref.edit().apply {
            putString("students_data", json)
            apply()
        }
    }

    override fun load(): MutableList<Student> {
        val sharedPref = context.getSharedPreferences("library_prefs", Context.MODE_PRIVATE)
        val json = sharedPref.getString("students_data", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Student>>() {}.type
            Gson().fromJson(json, type)
        } else mutableListOf()
    }
}

/*------------------ LIBRARY MANAGER ------------------*/
class LibraryManager(private val repository: StudentRepository) {
    val students: MutableList<Student> = repository.load().toMutableList()
        .ifEmpty {
            mutableListOf(
                Student("Nguyen Van A"),
                Student("Nguyen Thi B"),
                Student("Nguyen Van C")
            )
        }

    val allBooks: MutableList<Book> = mutableListOf(
        Book(1, "Sách 01"),
        Book(2, "Sách 02"),
        Book(3, "Sách 03"),
        Book(4, "Sách 04"),
        Book(5, "Sách 05")
    )

    fun borrowBooks(student: Student, books: List<Book>) {
        student.borrowBooks(books)
        repository.save(students)
    }
}

/*------------------ ACTIVITY ------------------*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = SharedPrefStudentRepository(this)
        val manager = LibraryManager(repo)

        setContent {
            MaterialTheme {
                LibraryApp(manager)
            }
        }
    }
}

/*------------------ COMPOSABLE APP ------------------*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryApp(manager: LibraryManager) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "HỆ THỐNG",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                "QUẢN LÝ THƯ VIỆN",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "quanly",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("quanly") { LibraryUI(manager) }
            composable("sach") { BookListScreen(manager) }
            composable("sinhvien") { StudentListScreen(manager) }
        }
    }
}

/*------------------ BOTTOM NAVIGATION ------------------*/
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

/*------------------ LIBRARY UI ------------------*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryUI(manager: LibraryManager) {
    var selectedStudent by remember { mutableStateOf(manager.students.first()) }
    var showStudentList by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sinh viên",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
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
                modifier = Modifier.height(IntrinsicSize.Min).wrapContentWidth(),
                shape = RoundedCornerShape(8.dp)
            ) { Text("Thay đổi", color = Color.White) }
        }

        if (showStudentList) {
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                    .padding(4.dp)
                    .heightIn(max = 200.dp)
            ) {
                items(manager.students) { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedStudent = student
                                showStudentList = false
                            }
                            .padding(8.dp)
                    ) { Text(student.name, fontSize = 16.sp) }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
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
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                )
            } else {
                LazyColumn {
                    items(selectedStudent.borrowedBooks) { book ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
        ) { Text("Thêm", color = Color.White) }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {
                Button(onClick = {
                    val selected = manager.allBooks.filter { it.selected }
                    manager.borrowBooks(selectedStudent, selected)
                    showAddDialog = false
                }) { Text("Xác nhận") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) { Text("Hủy") }
            },
            title = { Text("Chọn sách muốn mượn") },
            text = {
                LazyColumn {
                    items(manager.allBooks) { book ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        ) {
                            Checkbox(checked = book.selected, onCheckedChange = { book.selected = it })
                            Text(book.title)
                        }
                    }
                }
            }
        )
    }
}

/*------------------ BOOK LIST SCREEN ------------------*/
@Composable
fun BookListScreen(manager: LibraryManager) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Danh sách Sách", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(manager.allBooks) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) { Text(book.title, modifier = Modifier.padding(12.dp)) }
            }
        }
    }
}

/*------------------ STUDENT LIST SCREEN ------------------*/
@Composable
fun StudentListScreen(manager: LibraryManager) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Danh sách Sinh viên", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(manager.students) { s ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
    val fakeManager = LibraryManager(object : StudentRepository {
        override fun save(students: List<Student>) {}
        override fun load(): MutableList<Student> = mutableListOf(
            Student("Nguyen Van A"),
            Student("Nguyen Thi B")
        )
    })
    MaterialTheme { LibraryApp(fakeManager) }
}
