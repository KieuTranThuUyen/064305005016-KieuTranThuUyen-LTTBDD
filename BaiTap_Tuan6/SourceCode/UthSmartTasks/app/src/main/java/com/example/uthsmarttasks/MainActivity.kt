package com.example.uthsmarttasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.shadow

data class Attachment(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("fileName") @Expose val fileName: String,
    @SerializedName("fileUrl") @Expose val fileUrl: String
)

data class SubTask(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("title") @Expose val title: String,
    @SerializedName("isCompleted") @Expose val isCompleted: Boolean
)

data class Task(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("title") @Expose val title: String,
    @SerializedName("description") @Expose val description: String? = null,
    @SerializedName("status") @Expose val status: String? = null,
    @SerializedName("priority") @Expose val priority: String? = null,
    @SerializedName("category") @Expose val category: String? = null,
    @SerializedName("dueDate") @Expose val dueDate: String? = null,
    @SerializedName("attachments") @Expose val attachments: List<Attachment>? = emptyList(),
    @SerializedName("subtasks") @Expose val subtasks: List<SubTask>? = emptyList()
)

data class TaskResponse(
    @SerializedName("isSuccess") @Expose val isSuccess: Boolean,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("data") @Expose val data: List<Task>
)

data class SingleTaskResponse(
    @SerializedName("isSuccess") @Expose val isSuccess: Boolean,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("data") @Expose val data: Task
)

object RetrofitClient {
    private const val BASE_URL = "https://amock.io/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: TaskApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(TaskApi::class.java)
}

// ==================== API SERVICE ====================
interface TaskApi {
    @GET("api/researchUTH/tasks")
    suspend fun getTasks(): Response<TaskResponse>

    @GET("api/researchUTH/task/{id}")
    suspend fun getTask(@Path("id") id: Int): Response<SingleTaskResponse>

    @DELETE("api/researchUTH/task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
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
fun EmptyListView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "No tasks",
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(20.dp)).background(Color(0xFFF5F5F5)).padding(24.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text("No Tasks Yet!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
        Spacer(Modifier.height(8.dp))
        Text("Stay productive—add something to do", fontSize = 14.sp, color = Color(0xFF757575), textAlign = TextAlign.Center)
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    val bgColor = when (task.status?.lowercase()) {
        "in progress" -> Color(0xFFFFF3E0)
        "pending" -> Color(0xFFE8F5E8)
        "completed" -> Color(0xFFE3F2FD)
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
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.status?.equals("completed", ignoreCase = true) == true,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2196F3))
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF212121)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = task.description ?: "No description",
                        fontSize = 13.sp,
                        color = Color(0xFF757575),
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // DÒNG NGÀY GIỜ + STATUS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ${task.status ?: "Unknown"}",
                    fontSize = 12.sp,
                    color = Color(0xFF424242),
                    fontWeight = FontWeight.Medium
                )

                // ĐỊNH DẠNG GIỜ: "14:00 25/03-26"
                val timeText = task.dueDate?.let { formatDueDate(it) } ?: ""
                Text(
                    text = timeText,
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
fun formatDueDate(dueDate: String): String {
    // Input: "2024-03-25T14:00:00"
    return try {
        val parts = dueDate.split("T")
        if (parts.size < 2) return dueDate

        val date = parts[0].split("-")
        val time = parts[1].substring(0, 5) // "14:00"

        val day = date[2]
        val month = date[1]
        val year = date[0].takeLast(2) // "26"

        "$time $day/$month-$year"
    } catch (e: Exception) {
        dueDate
    }
}
@Composable
fun DetailHeader(task: Task) {
    Column(modifier = Modifier.padding(16.dp)) {
        // ======= TIÊU ĐỀ & MÔ TẢ =======
        Text(
            task.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            task.description ?: "No description available",
            fontSize = 14.sp,
            color = Color(0xFF757575)
        )
        Spacer(Modifier.height(16.dp))

        // ======= HÀNG THÔNG TIN (CATEGORY / STATUS / PRIORITY) =======
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFEBEE))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ---- CATEGORY ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Category",
                        fontSize = 10.sp,
                        color = Color(0xFF757575)
                    )
                    Text(
                        text = task.category ?: "Unknown",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            // ---- STATUS ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Status",
                        fontSize = 10.sp,
                        color = Color(0xFF757575)
                    )
                    Text(
                        text = task.status ?: "Unknown",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2)
                    )
                }
            }

            // ---- PRIORITY ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.PriorityHigh,
                    contentDescription = null,
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Priority",
                        fontSize = 10.sp,
                        color = Color(0xFF757575)
                    )
                    Text(
                        text = task.priority ?: "Unknown",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC62828)
                    )
                }
            }
        }
    }
}


@Composable
fun SubtaskItem(subtask: SubTask) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = subtask.isCompleted,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF2196F3),
                uncheckedColor = Color(0xFF757575)
            )
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = subtask.title,
            fontSize = 14.sp,
            color = if (subtask.isCompleted) Color(0xFF9E9E9E) else Color.Black,
            textDecoration = if (subtask.isCompleted) TextDecoration.LineThrough else null
        )
    }
}


@Composable
fun AttachmentItem(attachment: Attachment) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp)).background(Color(0xFFF5F5F5)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Attachment, contentDescription = null, tint = Color(0xFF757575), modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Text(attachment.fileName, fontSize = 14.sp, color = Color(0xFF424242), fontWeight = FontWeight.Medium)
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
            FloatingActionButton(
                onClick = { navController.navigate("add") },
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(60.dp)
                    .offset(y = 28.dp)
            ) {
                Icon(Icons.Default.Add, "Add", modifier = Modifier.size(30.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 16.dp, // BÓNG ĐỔ MẠNH
                modifier = Modifier
                    .height(84.dp)
                    .clip(RoundedCornerShape(28.dp)) // BO TRÒN 4 GÓC
                    .padding(horizontal = 16.dp, vertical = 8.dp) // CÁCH MÉP MÀN HÌNH
                    .shadow(12.dp, RoundedCornerShape(28.dp)) // BÓNG ĐỔ ĐẸP
            ) {
                NavigationBarItem(selected = true,  onClick = {}, icon = { Icon(Icons.Default.Home, "Home") },     label = { Text("Home") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.DateRange, "Calendar") }, label = { Text("Calendar") })
                Spacer(modifier = Modifier.width(88.dp))
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.AutoMirrored.Filled.List, "Tasks") }, label = { Text("Tasks") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Settings, "Settings") }, label = { Text("Settings") })
            }
        }

    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.9f)), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF2196F3), strokeWidth = 5.dp)
                    }
                }
                state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                state.tasks.isEmpty() -> EmptyListView()
                else -> LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(painter = painterResource(R.drawable.uth), "UTH Logo",
                                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text("UTH SmartTasks", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                                    Text("A simple and efficient to-do app", fontSize = 12.sp, color = Color(0xFF757575))
                                }
                            }
                            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF5F5F5)).clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Notifications, "Notifications", tint = Color(0xFF757575), modifier = Modifier.size(22.dp))
                            }
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
    var showDeleteDialog by remember { mutableStateOf(false) } // 🔹 Biến điều khiển dialog

    LaunchedEffect(Unit) { scope.launch { state.fetch() } }

    if (state.isDeleted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Task đã được xóa!")
                Spacer(Modifier.height(16.dp))
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
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) { // 🔹 Hiện dialog
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            tint = Color(0xFFB00020)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF2196F3), strokeWidth = 5.dp)
                    }
                }
                state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error)
                state.task != null -> {
                    val task = state.task!!
                    LazyColumn {
                        item { DetailHeader(task) }
                        item {
                            Text(
                                "Subtasks",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                            )
                            if (task.subtasks.isNullOrEmpty()) {
                                Text("No subtasks", modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray)
                            } else {
                                task.subtasks.forEach { subtask ->
                                    SubtaskItem(subtask)
                                }
                            }
                        }
                        item {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Attachments",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                            )
                            if (task.attachments.isNullOrEmpty()) {
                                Text("No attachments", modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray)
                            } else {
                                task.attachments.forEach { attachment ->
                                    AttachmentItem(attachment)
                                }
                            }
                        }
                    }
                }
            }

            // 🔹 HỘP THOẠI XÁC NHẬN XÓA
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Xác nhận xóa") },
                    text = { Text("Bạn có chắc muốn xóa task này không?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                scope.launch { state.delete { navController.popBackStack() } }
                            }
                        ) {
                            Text("Xóa", color = Color(0xFFB00020))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Hủy")
                        }
                    }
                )
            }
        }
    }
}


// ==================== THEME + MAIN ====================
@Composable
fun UTHSmartTasksTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Color(0xFF2196F3),
        background = Color(0xFFF5F5F5),
        surface = Color.White,
        error = Color(0xFFB00020)
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
                    composable("detail/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        TaskDetailScreen(navController, id)
                    }
                }
            }
        }
    }
}