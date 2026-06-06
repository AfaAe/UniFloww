package com.example.uniflow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.uniflow.ui.theme.UniFlowTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.core.net.toUri
import java.time.Month

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniFlowTheme {
                //Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                FrBackgroundYo {
                    AddingScreen()
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
    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top) {
        Box(
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp).background(
                Color(0xFFD9D9D9),
                shape = RoundedCornerShape(15),
            )
        ) {
            Text(
                text = "ВЫСТАВЛЯЙТЕ ДАТУ СООТВЕТСТВУЮЩУЮ НЫНЕШНЕМУ ГОДУ!",
                fontSize = 20.sp,
                color = Color.Black,
            )
        }
    }
}

