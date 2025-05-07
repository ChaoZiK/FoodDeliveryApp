package com.tranthephong.fooddeliveryapp.Activity.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranthephong.fooddeliveryapp.R

@Composable
fun BottomMenu(modifier: Modifier, onItemClick: (() -> Unit)?){
    Row(modifier = modifier
        .padding(start = 16.dp, end = 16.dp)
        .background(colorResource(R.color.green),
            shape = RoundedCornerShape(10.dp)
        ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        BottomMenuItem(icon = painterResource(R.drawable.home), onItemClick = onItemClick)
        BottomMenuItem(icon = painterResource(R.drawable.favorite), onItemClick = onItemClick)
        BottomMenuItem(icon = painterResource(R.drawable.cart), onItemClick = onItemClick)
        BottomMenuItem(icon = painterResource(R.drawable.profile), onItemClick = onItemClick)


    }
}

@Composable
fun BottomMenuItem(
    icon:Painter,
    onItemClick:(()->Unit)?=null)
{
    Column (modifier = Modifier
        .height(70.dp)
        .clickable { onItemClick?.invoke() }
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}