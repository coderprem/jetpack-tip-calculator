package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column {
                    TopHeader(1.0)
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipCalculatorTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier, color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(all = 24.dp)
            .clip(CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold, fontSize = 24.sp
                ),
            )
            Text(
                text = "$$total",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainContent() {
    val totalBillState = remember {
        mutableStateOf("")
    }
    Surface(
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 6.dp)
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = Color(0xFFFFFFFF),
        border = BorderStroke(1.dp, color = Color.Gray)
    ) {
        val value by remember {
            mutableStateOf("value")
        }
        Column {
            var textValue by remember {
                mutableStateOf("")
            }
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                placeholder = {
                    Text(text = "Enter Bill")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AttachMoney,
                        contentDescription = "Dollar sign",
                        tint = Color.Gray
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                keyboardOptions = KeyboardOptions(autoCorrect = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done),
                singleLine = true,
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculatorTheme {
        MyApp {
            Text(text = "hello there")
        }
    }
}