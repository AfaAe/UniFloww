package com.example.uniflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniflow.ui.theme.UniFlowTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

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
            onClick = { /* бебебе */ },
            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),contentColor = Color.Black)

        ){
            Text(text = "GitHub", fontSize = 30.sp, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(15),
            onClick = { /* бебебе */ },
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
    val UsTask = remember{mutableStateOf("")}
    val DateTask = remember{mutableStateOf("")}

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
            text = "Оповестить\n\n$formattedDate",
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
            modifier = Modifier.padding(start = 16.dp, top = 30.dp, end = 16.dp).fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFD9D9D9),
                focusedContainerColor = Color(0xFFD9D9D9)
            )
        )
        Row {
            TextField(
                value = DateTask.value,
                onValueChange = { newText -> DateTask.value = newText },
                textStyle = TextStyle(fontSize = 25.sp),
                shape = RoundedCornerShape(15),
                modifier = Modifier.padding(start = 16.dp, top = 30.dp, end = 290.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFD9D9D9),
                    focusedContainerColor = Color(0xFFD9D9D9)
                )
            )
            Button(
                modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 16.dp),
                shape = RoundedCornerShape(15),
                onClick = { /* бебебе */ },
                colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),contentColor = Color.Black)

            ) {
                Text(text = "+", fontSize = 30.sp, textAlign = TextAlign.Center, lineHeight = 31.sp)
            }
            //ВНИМАНИЕ!!!!!Не забудь поправить кнопку,её нет на экране
        }
    }
}

