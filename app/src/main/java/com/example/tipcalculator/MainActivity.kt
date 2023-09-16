package com.example.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.components.InputField
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import com.example.tipcalculator.util.calculateTotalPerPerson
import com.example.tipcalculator.util.calculateTotalTip
import com.example.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column {
//                    TopHeader(1.0)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainContent() {
    BillForm() { billAmount ->
        Log.d("Tag", "${billAmount.toInt() * 100}")
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {} // here we have a trailing lambda, when we will call BillForm compose, we can get the value of bill
) {

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val sliderPositionState = remember {
        mutableFloatStateOf(70f)
    }
    var numberOfPeople by remember {
        mutableIntStateOf(1)
    }
    val tipPercentage = (sliderPositionState.floatValue * 1).toInt()
    val keyboardController = LocalSoftwareKeyboardController.current
    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    TopHeader(totalPerPersonState.value)
    Surface(
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 6.dp)
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = Color(0xFFFFFFFF),
        border = BorderStroke(1.dp, color = Color.Gray)
    ) {
        Column(
            modifier = modifier.padding(6.dp), verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                isSingleLine = true,
                isEnable = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    // todo - onValueChanged
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if (validState) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Split",
                    modifier = modifier
                        .padding(start = 12.dp)
                        .align(alignment = CenterVertically),
                    style = TextStyle(fontSize = 20.sp),
                )
                Spacer(modifier = modifier.width(150.dp))
                Row(
                    modifier = modifier.padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                        if (numberOfPeople > 1) numberOfPeople -= 1
                        totalPerPersonState.value =
                            calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = numberOfPeople,
                                tipPercentage = tipPercentage
                            )

                    })
                    Text(text = "$numberOfPeople", modifier.padding(horizontal = 12.dp))
                    RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                        numberOfPeople += 1
                        totalPerPersonState.value =
                            calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = numberOfPeople,
                                tipPercentage = tipPercentage
                            )

                    })
                }
            }
            // tip row
            Row(
                modifier = modifier.padding(vertical = 12.dp, horizontal = 2.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tip",
                    style = TextStyle(fontSize = 20.sp),
                    modifier = modifier
                        .padding(start = 12.dp)
                        .align(alignment = CenterVertically),
                )
                Spacer(modifier = modifier.width(200.dp))
                Text(
                    text = "${tipAmountState.doubleValue}",
                    style = TextStyle(fontSize = 20.sp),
                    modifier = modifier
                        .padding(start = 12.dp)
                        .align(alignment = CenterVertically),
                )
            }
            // slider
            Column(
                modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$tipPercentage %")
                Slider(
                    value = sliderPositionState.floatValue,
                    onValueChange = {
                        sliderPositionState.floatValue = it
                        tipAmountState.doubleValue =
                            calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)
                        totalPerPersonState.value =
                            calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = numberOfPeople,
                                tipPercentage = tipPercentage
                            )
                        Log.d("tipAmount", "${tipAmountState.doubleValue}")
                    },
                    modifier.padding(horizontal = 12.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 100,
                    valueRange = 0f..100f
                )
            }
            }
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