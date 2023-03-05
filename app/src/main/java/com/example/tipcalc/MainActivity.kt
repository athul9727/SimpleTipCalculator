package com.example.tipcalc

import android.os.Bundle
import android.view.Display.Mode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalc.ui.theme.TipCalcTheme
import com.example.tipcalc.utils.CustomInputField
import com.example.tipcalc.utils.CustomRoundIconButton
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                    myApp {
                        MainContent()
                    }
        }
    }
}

@Composable
fun myApp(contentMyApp: @Composable () -> Unit) {
    TipCalcTheme {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            contentMyApp()
        }
    }
}


@Composable
fun TopHeader(total:Double = 0.0){
    Surface(
        color = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .padding(all = 15.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))) {
       Column(
           modifier = Modifier.padding(12.dp),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {

           val totalNew = "%.2f".format(total)

           Text(text = "Total Per Person", style = MaterialTheme.typography.h5)
           Text(text = "$$totalNew", style =   MaterialTheme.typography.h4, fontWeight = FontWeight.Bold)
       }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContent(){

    val splitByState = remember {
        mutableStateOf(1)
    }
    val tipAmtState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val range = IntRange(start = 1, endInclusive = 100)
    
    
    Column(modifier = Modifier.padding(all = 12.dp)) {

        BillForm(
            range = range,
            splitByState = splitByState,
            tipAmtState = tipAmtState,
            totalPerPersonState = totalPerPersonState
           ){ billAmt->

        }




  }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range:IntRange = 1..100,
    splitByState:MutableState<Int>,
    tipAmtState:MutableState<Double>,
    totalPerPersonState:MutableState<Double>,

    onValChange:(String) -> Unit = {

    }//callback,

){

    val totalBillState = remember {
        mutableStateOf("0")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardcontroller = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercent = (sliderPositionState.value*100).toInt()



    TopHeader(total = totalPerPersonState.value)


    Surface( modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 2.dp, color = Color.Gray)
    ){

        Column(modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
            CustomInputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if(!validState) return@KeyboardActions

                    onValChange(totalBillState.value.trim())//call

                    keyboardcontroller?.hide()
                    

                    
                }
            )

            if(validState){
                
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start)
                    {

                    Text(text = "Split",modifier = Modifier.align(
                        alignment = Alignment.CenterVertically))
                        
                    Spacer(modifier = Modifier.width(120.dp))    
                        
                        Row(modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End) {

                            CustomRoundIconButton(
                                modifier = Modifier,
                                imageVector = Icons.Default.Remove,
                                onClick = {

                                    if(splitByState.value>range.first)
                                       splitByState.value = splitByState.value - 1
                                    else
                                        splitByState.value = 1

                                    totalPerPersonState.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitByState.value,
                                        tipPercent = tipPercent)

                                }
                            )
                            
                            Text(
                                text = "${splitByState.value}",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .align(alignment = Alignment.CenterVertically),
                                )

                            CustomRoundIconButton(
                                modifier = Modifier,
                                imageVector = Icons.Default.Add,
                                onClick = {

                                   if(splitByState.value<range.last)
                                       splitByState.value = splitByState.value + 1

                                    totalPerPersonState.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitByState.value,
                                        tipPercent = tipPercent)

                                }
                            )

                        }

                    }

            }
            else{
                Box(){}
            }

            Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)){
                Text(text = "Tip",modifier = Modifier.align(alignment = Alignment.CenterVertically) )
                Spacer(modifier = Modifier.width(width = 200.dp))
                Text(text = "$${tipAmtState.value}",modifier = Modifier.align(alignment = Alignment.CenterVertically) )
            }
            
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {

                Text(text = "${tipPercent}%")

                Spacer(modifier = Modifier.height(14.dp))

                Slider(value = sliderPositionState.value, onValueChange = {
                    newValue -> sliderPositionState.value = newValue
                    
                    tipAmtState.value = calculateTotalTip(
                        totalBill = totalBillState.value.toDouble(),
                        tipPercent = tipPercent)

                    totalPerPersonState.value = calculateTotalPerPerson(
                        totalBill = totalBillState.value.toDouble(),
                        splitBy = splitByState.value,
                        tipPercent = tipPercent)




                },modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                steps = 5,
                onValueChangeFinished = {

                })
            }

        }

    }
}

fun calculateTotalTip(totalBill:Double,tipPercent:Int): Double {
    return if(totalBill>1){
        (totalBill * tipPercent)/100
    } else{  0.0  }
}

fun calculateTotalPerPerson(
    totalBill: Double,
    splitBy:Int,
    tipPercent:Int
    ) : Double{

    val bill = calculateTotalTip(totalBill = totalBill,tipPercent = tipPercent) + totalBill

    return (bill/splitBy)

}
