package com.example.tipcalc.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin


@Composable
fun CustomInputField(
    modifier:Modifier = Modifier,
    valueState:MutableState<String>,
    labelId:String,
    enabled:Boolean,
    isSingleLine:Boolean,
    keyBoardType:KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
 ){
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
        label = {Text(text = labelId)},
        leadingIcon = { Icon(imageVector = Icons.Rounded.AttachMoney, contentDescription ="Money" )},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = keyBoardType, imeAction = imeAction),
        keyboardActions = onAction,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()

    )
}

val IconButtonSizeModifier = Modifier.size(40.dp)

@Composable
fun CustomRoundIconButton(
    modifier: Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    tintColor:Color = Color.Black.copy(alpha = 0.8f),
    backgroundColor:Color = MaterialTheme.colors.background,
    elevation: Dp = 4.dp
){

    Card(modifier = modifier
        .padding(4.dp)
        .clickable { onClick.invoke() }
        .then(IconButtonSizeModifier),
    shape = CircleShape,
    backgroundColor = backgroundColor,
    elevation = elevation,
    ){
        Icon(
            imageVector = imageVector,
            contentDescription = "add or remove icon",
            tint = tintColor)

    }

}
