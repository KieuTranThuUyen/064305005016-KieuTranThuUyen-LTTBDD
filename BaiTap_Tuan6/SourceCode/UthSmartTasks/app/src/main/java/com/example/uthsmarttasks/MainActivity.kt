package com.example.uthsmarttasks

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

// ==================== DATA MODELS ====================
data class Attachment(
    @SerializedName("id") val id: Int,
    @SerializedName("fileName") val fileName: String,
    @SerializedName("fileUrl") val fileUrl: String
)

data class SubTask(
    val id: Int,
    val title: String,
    val completed: Boolean
)

data class Task(
    val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("status") val status: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("dueDate") val dueDate: String? = null,
    @SerializedName("attachments") val attachments: List<Attachment>? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("subtasks") val subtasks: List<SubTask>? = null
)

data class TaskResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Task>
)

data class SingleTaskResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Task
)

// ==================== API SERVICE ====================
interface TaskApi {
    @GET("api/researchUTH/tasks")
    suspend fun getTasks(): Response<TaskResponse>

    @GET("api/researchUTH/task/{id}")
    suspend fun getTask(@Path("id") id: Int): Response<SingleTaskResponse>

    @DELETE("api/researchUTH/task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "https://amock.io/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    val api: TaskApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)
}

// ==================== STATE ====================
class TaskListState {
    var tasks by mutableStateOf<List<Task>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    suspend fun fetch() {
        isLoading = true
        error = null
        try {
            val response = RetrofitClient.api.getTasks()
            if (response.isSuccessful) {
                tasks = response.body()?.data ?: emptyList()
            } else {
                error = "Lỗi: ${response.code()}"
            }
        } catch (e: Exception) {
            error = e.message ?: "Lỗi kết nối"
        } finally {
            isLoading = false
        }
    }
}

class TaskDetailState(private val taskId: Int) {
    var task by mutableStateOf<Task?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isDeleted by mutableStateOf(false)

    suspend fun fetch() {
        isLoading = true
        try {
            val response = RetrofitClient.api.getTask(taskId)
            if (response.isSuccessful) {
                task = response.body()?.data
            } else {
                error = "Lỗi: ${response.code()}"
            }
        } catch (e: Exception) {
            error = e.message ?: "Lỗi kết nối"
        } finally {
            isLoading = false
        }
    }

    suspend fun delete(onSuccess: () -> Unit) {
        try {
            val response = RetrofitClient.api.deleteTask(taskId)
            if (response.isSuccessful) {
                isDeleted = true
                onSuccess()
            } else {
                error = "Xóa thất bại"
            }
        } catch (e: Exception) {
            error = e.message
        }
    }
}

// ==================== UI COMPONENTS ====================
@Composable
fun StatusBadge(text: String, background: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun EmptyListView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.EventNote, // ĐÃ SỬA
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFF9E9E9E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("No Tasks Yet!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            "Stay productive—add something to do",
            fontSize = 14.sp,
            color = Color(0xFF757575)
        )
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    val bgColor = when (task.status.lowercase()) {
        "in progress" -> Color(0xFFFFF3E0)
        "pending" -> Color(0xFFE8F5E8)
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = task.status == "completed",
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6200EE))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(task.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Status: ${task.status}", fontSize = 12.sp, color = Color(0xFF424242))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(task.dueDate ?: "", fontSize = 12.sp, color = Color(0xFF757575))
                }
            }
        }
    }
}

@Composable
fun DetailHeader(task: Task) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(task.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            StatusBadge("Category", Color(0xFFFFF3E0), Color(0xFFE65100))
            Spacer(Modifier.width(8.dp))
            StatusBadge(task.category ?: "Work", Color(0xFFE8F5E8), Color(0xFF2E7D32))
            Spacer(Modifier.width(8.dp))
            StatusBadge("Status", Color(0xFFE3F2FD), Color(0xFF1976D2))
            Spacer(Modifier.width(8.dp))
            StatusBadge(task.status, Color(0xFFFFEBEE), Color(0xFFC62828))
            Spacer(Modifier.width(8.dp))
            StatusBadge("Priority", Color(0xFFFFF3E0), Color(0xFFE65100))
            Spacer(Modifier.width(8.dp))
            StatusBadge(task.priority, Color(0xFFFFEBEE), Color(0xFFC62828))
        }
    }
}

@Composable
fun SubtaskItem(subtask: SubTask) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = subtask.completed, onCheckedChange = null)
        Spacer(Modifier.width(8.dp))
        Text(subtask.title, fontSize = 14.sp, color = if (subtask.completed) Color.Gray else Color.Black)
    }
}

@Composable
fun AttachmentItem(attachment: Attachment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Attachment, contentDescription = null, tint = Color(0xFF757575), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(attachment.fileName, fontSize = 14.sp, color = Color(0xFF424242))
    }
}

// ==================== SCREENS ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavHostController) {
    val state = remember { TaskListState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { scope.launch { state.fetch() } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("List", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Add task */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.DateRange, null) })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.AutoMirrored.Filled.List, null) }) // ĐÃ SỬA
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Settings, null) })
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                state.tasks.isEmpty() -> EmptyListView()
                else -> LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SmartToy, null, tint = Color(0xFF6200EE))
                                Spacer(Modifier.width(8.dp))
                                Text("UTH", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("SmartTasks", color = Color(0xFF6200EE), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Icon(Icons.Default.Notifications, null)
                        }
                    }
                    items(state.tasks) { task ->
                        TaskCard(task) { navController.navigate("detail/${task.id}") }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(navController: NavController, taskId: Int) {
    val state = remember { TaskDetailState(taskId) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { scope.launch { state.fetch() } }

    if (state.isDeleted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Task đã được xóa!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) { Text("Quay lại") }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error)
                state.task != null -> {
                    val task = state.task!!
                    LazyColumn {
                        item { DetailHeader(task) }

                        item {
                            Text("Subtasks", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp))
                            task.subtasks?.forEach { subtask ->
                                SubtaskItem(subtask)
                            } ?: Text("No subtasks", modifier = Modifier.padding(horizontal = 16.dp))
                        }

                        item {
                            Spacer(Modifier.height(16.dp))
                            Text("Attachments", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp))
                            task.attachments?.forEach { attachment ->
                                AttachmentItem(attachment)
                            } ?: Text("No attachments", modifier = Modifier.padding(horizontal = 16.dp))
                        }

                        item {
                            Spacer(Modifier.height(32.dp))
                            Button(
                                onClick = { scope.launch { state.delete { navController.popBackStack() } } },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                            ) {
                                Icon(Icons.Default.Delete, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Xóa Task")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== THEME + MAIN ====================
@Composable
fun UTHSmartTasksTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFFF5F5F5),
        surface = Color.White,
        error = Color(0xFFB00020),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        onError = Color.White,
        surfaceVariant = Color(0xFFF5F5F5),
        outline = Color(0xFF757575)
    )

    MaterialTheme(colorScheme = colorScheme, typography = Typography(), content = content)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UTHSmartTasksTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") { TaskListScreen(navController) }
                    composable(
                        "detail/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        TaskDetailScreen(navController, id)
                    }
                }
            }
        }
    }
}