package com.example.uniflow

import android.content.Intent
//import android.graphics.drawable.Icon
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniflow.ui.theme.UniFlowTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniFlowTheme {
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
                            0 -> FrBackgroundYo { SettingsScreen() }
                            1 -> FrBackgroundYo { AddingScreen() }
                            2 -> FrBackgroundYo { CalendarScreen() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
//функции для экранов
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
fun SettingsScreen(){
    val context = LocalContext.current
    val reqPermission = remeNotifPerm { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Уведомления включены", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Уведомления выключены.", Toast.LENGTH_SHORT).show()
        }}

        Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp), verticalArrangement = Arrangement.Top) {
        Button(
            modifier = Modifier.fillMaxWidth().height(75.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(15),
            onClick = { /* бебебе */ },
            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),contentColor = Color.Black)

        ) {
            Text(text = "Очистить задачи", fontSize = 30.sp, textAlign = TextAlign.Center)
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
            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),contentColor = Color.Black)

        ){
            Text(text = "GitHub", fontSize = 30.sp, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(15),
            onClick = {reqPermission()},
            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),contentColor = Color.Black)

        ) {
            Text(text = "Включить уведомления", fontSize = 30.sp, textAlign = TextAlign.Center, lineHeight = 31.sp)
        }
            Text(
                text = "версия\n1.0",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 430.dp),
                textAlign = TextAlign.Center,
                color = Color(127,112,107),
            )
    }
}

@Composable
fun AddingScreen(){
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
                    if (DateTask.value.toString().length == 2 && DateTask2.value.toString().length == 2) {
                        UsDate.value = LocalDate.of(2026, DateTask2.value.toInt(), DateTask.value.toInt())
                    }
                },
                //КОГДА СДЕЛАЕШЬ ЛИСТЫ, СДЕЛАЙ ПРОВЕРКУ НА СУЩЕСТВОВАНИЕ КНОПКИ ПО АЙДИ!!!!!!!!!!
                colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),contentColor = Color.Black)
            ) {
                Text(text = "✓", fontSize = 30.sp, textAlign = TextAlign.Center, color = Color(146,146,146), fontWeight = FontWeight.Light)
                /*Image(
                    imageVector = ImageVector.vectorResource(R.drawable.galochka3),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "Добавить время"
                )*/
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(top = 50.dp)) {
            Button(
                modifier = Modifier.size(150.dp),
                shape = RoundedCornerShape(15),
                onClick = { /* бебебе */ },
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
fun CalendarScreen(){
    val СurrentMonth = remember { mutableStateOf(LocalDate.now().monthValue) }
    val MonthNames = listOf("Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь")
    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.padding(top = 24.dp).size(300.dp, 50.dp).background(Color(208, 255, 255), shape = RoundedCornerShape(15))) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Раннее"
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
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Позднее"
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp).background(
                Color(0xFFD9D9D9),
                shape = RoundedCornerShape(15),
            )
        ) {

        }
    }
}

//эт классы и тд для 3 экрана
data class Task(
    val id: String = "",
    val textTask: String = "",
    var isCompleted: Boolean = false,
)

data class TaskDay(
    val idD: Int,
    val tasks: MutableList<Task> = mutableListOf()
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
        }
    }
}

val year: MutableList<List<TaskDay>> = mutableListOf()
@Composable
fun AddingDaysToMonths() {
    for (i in 1..12) {
        val daysList = mutableListOf<TaskDay>()
        if (i in setOf(1, 3, 5, 7, 8, 10, 12)){
            for (j in 1..31) {
                val taski = mutableListOf<Task>()
                daysList.add(TaskDay(idD = j,taski))
            }
        }
        else if (i in setOf(4, 6, 9, 11)){
            for (j in 1..30) {
                val taski = mutableListOf<Task>()
                daysList.add(TaskDay(idD = j,taski))
            }
        }
        else if(i == 2){
            for (j in 1..28) {
                val taski = mutableListOf<Task>()
                daysList.add(TaskDay(idD = j,taski))
            }
        }
        year.add(daysList)
    }
}

