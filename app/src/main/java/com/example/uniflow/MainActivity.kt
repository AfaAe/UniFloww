package com.example.uniflow

import android.content.Intent
import androidx.compose.material3.Icon
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniflow.ui.theme.UniFlowTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniFlowTheme {
                val year = remember { mutableStateListOf<MutableList<TaskDay>>() }
                var calendarRefreshTrigger by remember { mutableStateOf(0) }
                if (year.isEmpty()) { AddingDaysToMonths(year)}
                val pagerState = rememberPagerState(initialPage = 2, pageCount = { 3 })
                val coroutineScope = rememberCoroutineScope()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar(containerColor = Color(122, 105, 99)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(0)
                                        }
                                    },
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.settingsic),
                                        contentDescription = "Настройки",
                                        modifier = Modifier.size(60.dp),
                                        tint = if (pagerState.currentPage == 0) Color(208,255,255) else Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(1)
                                        }
                                    },
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.outline_add_24),
                                        contentDescription = "Добавление задачи",
                                        modifier = Modifier.size(60.dp),
                                        tint = if (pagerState.currentPage == 1) Color(208,255,255) else Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(2)
                                        }
                                    },
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.boxie),
                                        contentDescription = "Календарь",
                                        modifier = Modifier.size(60.dp),
                                        tint = if (pagerState.currentPage == 2) Color(208,255,255) else Color.White
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) { page ->
                        when (page) {
                            0 -> FrBackgroundYo { SettingsScreen(year) }
                            1 -> FrBackgroundYo {
                                AddingScreen(year) {
                                    calendarRefreshTrigger++
                                }
                            }
                            2 -> FrBackgroundYo {
                                CalendarScreen(year = year, refreshTrigger = calendarRefreshTrigger)
                            }
                        }
                    }
                }
            }
        }
    }
}

//функции для экранов
@Composable
fun remeNotifPerm(
    onResult: (Boolean) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> onResult(isGranted) }
    return remember {
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                onResult(true)
            }
        }
    }
}

//экраны
@Composable
fun FrBackgroundYo(content: @Composable () -> Unit){
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFA89A95)), contentAlignment = Alignment.Center){
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.logofrback),
            contentDescription = "Лого UniFlow",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().requiredHeightIn(min = 350.dp, max = 500.dp))
        content()
    }
}

@Composable
fun SettingsScreen(year: MutableList<MutableList<TaskDay>>) {
    var showClearDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val reqPermission = remeNotifPerm { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Уведомления включены", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Уведомления выключены.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            modifier = Modifier.fillMaxWidth().height(75.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(15),
            onClick = { showClearDialog = true },
            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9), contentColor = Color.Black)

        ) {
            Text(text = "Очистить задачи", fontSize = 30.sp, textAlign = TextAlign.Center)
        }
        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Подтверждение") },
                text = { Text("Вы уверены, что хотите очистить все задачи?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            year.forEach { month ->
                                month.forEach { taskDay ->
                                    taskDay.tasks.clear()
                                }
                            }
                            Toast.makeText(context, "Задачи очищены", Toast.LENGTH_SHORT).show()
                            showClearDialog = false
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text("Нет")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(75.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(15),
            onClick = {
                val webpage: Uri = Uri.parse("https://github.com/AfaAe/UniFloww")
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9), contentColor = Color.Black)

        ) {
            Text(text = "GitHub", fontSize = 30.sp, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(15),
            onClick = { reqPermission() },
            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9), contentColor = Color.Black)

        ) {
            Text(
                text = "Включить уведомления",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                lineHeight = 31.sp
            )
        }
        Text(
            text = "версия\n1.0",
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth().padding(top = 430.dp),
            textAlign = TextAlign.Center,
            color = Color(127, 112, 107),
        )

    }
}

@Composable
fun AddingScreen(year: MutableList<MutableList<TaskDay>>, onTaskAdded: () -> Unit = {}){

    val context = LocalContext.current
    val currentDate = LocalDate.now()
    val dateFormat = DateTimeFormatter.ofPattern("d MMMM").withLocale(Locale("ru", "RU"))
    val formattedDate = currentDate.format(dateFormat)

    val UsDate = remember{mutableStateOf(LocalDate.now())}
    val formattedDate2 = UsDate.value.format(dateFormat)

    val UsTask = remember{mutableStateOf("")}
    val DateTask = remember{mutableStateOf("")}
    val DateTask2 = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top) {
        Text(
            text = "Сегодня\n\n$formattedDate",
            fontSize = 45.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Light
        )
        Text(
            text = "Оповестить\n\n$formattedDate2",
            fontSize = 45.sp,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Light
        )
        TextField(
            value = UsTask.value,
            onValueChange = { newText -> UsTask.value = newText },
            textStyle = TextStyle(fontSize = 25.sp),
            shape = RoundedCornerShape(15),
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 30.dp, end = 16.dp),
            placeholder = { Text("Введите задачу...") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD9D9D9),
                focusedContainerColor = Color(0xFFD9D9D9)
            )
        )
        Row (modifier = Modifier.padding(start = 16.dp, top = 30.dp, end = 50.dp)){
            TextField(
                value = DateTask.value,
                onValueChange = { newValue ->
                    if(newValue.all {it.isDigit()}&&newValue.length<=2){ DateTask.value = newValue }},
                textStyle = TextStyle(fontSize = 25.sp),
                shape = RoundedCornerShape(15),
                modifier = Modifier.width(60.dp),
                placeholder = { Text("ДД") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFD9D9D9),
                    focusedContainerColor = Color(0xFFD9D9D9)
                )
            )
            TextField(
                value = DateTask2.value,
                onValueChange = { newValue ->
                    if(newValue.all {it.isDigit()}&&newValue.length<=2){ DateTask2.value = newValue }},
                textStyle = TextStyle(fontSize = 25.sp),
                shape = RoundedCornerShape(15),
                modifier = Modifier.width(80.dp).padding(start = 16.dp),
                placeholder = { Text("ММ") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFD9D9D9),
                    focusedContainerColor = Color(0xFFD9D9D9)
                )
            )
            Button(
                modifier = Modifier.size(width = 85.dp, height = 65.dp).padding(start = 16.dp),
                shape = RoundedCornerShape(15),
                onClick = {
                    if (DateTask.value.toString().length == 2 && DateTask2.value.toString().length == 2 && year.getOrNull(DateTask2.value.toInt() - 1)?.getOrNull(DateTask.value.toInt() - 1) != null) {
                        UsDate.value = LocalDate.of(2026, DateTask2.value.toInt(), DateTask.value.toInt())
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),contentColor = Color.Black)
            ) {
                Text(text = "✓", fontSize = 30.sp, textAlign = TextAlign.Center, color = Color(146,146,146), fontWeight = FontWeight.Light)
            }
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(top = 50.dp)) {
            Button(
                modifier = Modifier.size(150.dp),
                shape = RoundedCornerShape(15),
                onClick = {
                    if (DateTask.value.toString().length == 2 && DateTask2.value.toString().length == 2 && year.getOrNull(DateTask2.value.toInt() - 1)?.getOrNull(DateTask.value.toInt() - 1) != null && UsTask.value.isNotEmpty()) {
                        year[DateTask2.value.toInt() - 1][DateTask.value.toInt() - 1].AddTask(Task(textTask = UsTask.value))
                        UsTask.value = ""; DateTask.value = ""; DateTask2.value = "";
                        Toast.makeText(context, "Задача добавлена!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context, "Ошибка в дате или задача пуста", Toast.LENGTH_SHORT).show()}
                },
                colors = ButtonDefaults.buttonColors(Color(208, 255, 255), contentColor = Color.Black)
            ) {
                Text(text = "+", fontSize = 100.sp, textAlign = TextAlign.Center, color = Color(146,179,179), fontWeight = FontWeight.Light)
            }

        }
        Box(modifier = Modifier.fillMaxSize().padding(start = 125.dp, top = 40.dp, end = 30.dp).background(Color(0xFFD9D9D9))) {
            Text(
                text = "ВЫСТАВЛЯЙТЕ ДАТУ СООТВЕТСТВУЮЩУЮ НЫНЕШНЕМУ ГОДУ!",
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                color = Color.Black,
            )
        }
    }
}

@Composable
fun CalendarScreen(year: MutableList<MutableList<TaskDay>>, refreshTrigger: Int){
    val СurrentMonth = remember { mutableStateOf(LocalDate.now().monthValue) }
    val MonthNames = listOf("Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь")

    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.padding(top = 24.dp).size(300.dp, 50.dp).background(Color(208, 255, 255), shape = RoundedCornerShape(15))) {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (СurrentMonth.value > 1) {
                                СurrentMonth.value -= 1
                            } else {
                                СurrentMonth.value = 1
                            }
                        },
                        modifier = Modifier.size(80.dp).padding(bottom = 5.dp)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.polygon1),
                            modifier = Modifier.size(30.dp), contentDescription = "Раннее"
                        )
                    }
                    Text(
                        text = MonthNames[СurrentMonth.value - 1],
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = Color(89, 125, 125),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.size(150.dp,50.dp)
                    )
                    IconButton(
                        onClick = {
                            if (СurrentMonth.value < 12) {
                                СurrentMonth.value += 1
                            } else {
                                СurrentMonth.value = 12
                            }
                        },
                        modifier = Modifier.size(80.dp).padding(bottom = 5.dp)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.polygon2),
                            modifier = Modifier.size(30.dp), contentDescription = "Позднее"
                        )
                    }
                }
            }
        }
        Box(modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp).background(
                Color(0xFFD9D9D9), shape = RoundedCornerShape(5),)) {
            DaysDrawing(СurrentMonth.value-1,year = year, refreshTrigger = refreshTrigger)
        }
    }
}

//эт классы и тд для 3 экрана
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val textTask: String = "",
    var isCompleted: Boolean = false,
)

class TaskDay(
    val idD: Int,
    val tasks: MutableList<Task> = mutableStateListOf()
) {
    fun AddTask(task: Task) {
        tasks.add(task)
    }
    fun DeleteTask(taskId: String) {
        tasks.removeAll { it.id == taskId }
    }
    fun TaskStat(taskId: String) {
        val task = tasks.find { it.id == taskId }
        if (task != null) {
            task.isCompleted = !task.isCompleted
            val index = tasks.indexOf(task)
            if (index != -1) {
                tasks[index] = task
            }
        }
    }
    fun GetList(): MutableList<Task> {
        return tasks
    }
}

fun AddingDaysToMonths(year: MutableList<MutableList<TaskDay>>) {
    for (i in 1..12) {
        val daysList = mutableStateListOf<TaskDay>()
        if (i in setOf(1, 3, 5, 7, 8, 10, 12)){
            for (j in 1..31) {
                daysList.add(TaskDay(idD = j))
            }
        }
        else if (i in setOf(4, 6, 9, 11)){
            for (j in 1..30) {
                daysList.add(TaskDay(idD = j))
            }
        }
        else if(i == 2){
            for (j in 1..28) {
                daysList.add(TaskDay(idD = j))
            }
        }
        year.add(daysList)
    }
}

@Composable
fun DaysDrawing(month: Int, year: MutableList<MutableList<TaskDay>>, refreshTrigger: Int) {
    var selectedDay by remember(refreshTrigger) { mutableStateOf(-1) }
    var showTasks by remember(refreshTrigger) { mutableStateOf(false) }


    Column {
        for (row in 0..6) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0 until 5) {
                    val dayIndex = row * 5 + col
                    if (dayIndex < year[month].size) {
                        val dayNumber = dayIndex + 1
                        Button(
                            modifier = Modifier.size(67.dp,50.dp),
                            shape = RoundedCornerShape(15),
                            onClick = { selectedDay = dayNumber
                                showTasks = true},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedDay == dayNumber) { Color(208, 255, 255)
                                } else { Color(168, 154, 149)},
                                contentColor = if (selectedDay == dayNumber) { Color( 89, 125, 125)} else { Color.White}
                            )

                        ) {
                            Text(
                                text = dayNumber.toString(),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        if (showTasks && selectedDay != -1) {
            TasksDrawing(month,selectedDay-1, year = year, refreshTrigger = refreshTrigger)
        }
    }
}

@Composable
fun TasksDrawing(month2: Int, dayId: Int, year: MutableList<MutableList<TaskDay>>, refreshTrigger: Int) {
    var localRefreshTrigger by remember { mutableStateOf(0) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val taskList = remember(refreshTrigger, localRefreshTrigger, year[month2][dayId].tasks.size) {
        year[month2][dayId].GetList().toList()
    }

    Image(
        imageVector = ImageVector.vectorResource(R.drawable.rectangle_4),
        modifier = Modifier.size(380.dp, 30.dp).padding(start = 18.dp, end = 18.dp),
        contentDescription = "Полоса"
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = (dayId+1).toString() + ".",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        var a: Int = 0;
        for (task in year[month2][dayId].GetList()) {
            a += 1;
            Text(
                text = a.toString() + ") " + task.textTask,
                fontSize = 30.sp,
                textDecoration = if (task.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
                Button(
                    modifier = Modifier.size(70.dp,65.dp).padding(end = 5.dp),
                    shape = RoundedCornerShape(15),
                    onClick = { year[month2][dayId].TaskStat(task.id)
                        localRefreshTrigger++},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (task.isCompleted) {
                            Color.Gray
                        } else {
                            Color(47, 220, 41)
                        },
                        contentColor = Color.White
                    )
                ) {
                    if (task.isCompleted) {
                        Text(
                            text = "✕",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Light
                        )
                    } else {
                        Text(
                            text = "✓",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                Button(
                    modifier = Modifier.size(65.dp),
                    shape = RoundedCornerShape(15),
                    onClick = { taskToDelete = task },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(194, 39, 39),
                        contentColor = Color.White
                    )
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_outline_24),
                        modifier = Modifier.size(30.dp),
                        contentDescription = "Удалить задачу"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if (taskToDelete != null) {
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = { Text("Подтверждение") },
            text = { Text("Вы уверены, что хотите удалить эту задачу?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        taskToDelete?.let { task ->
                            year[month2][dayId].DeleteTask(task.id)
                            localRefreshTrigger++
                        }
                        taskToDelete = null
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToDelete = null }) {
                    Text("Нет")
                }
            }
        )
    }
}


