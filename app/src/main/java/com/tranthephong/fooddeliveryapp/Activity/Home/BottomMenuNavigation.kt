package com.tranthephong.fooddeliveryapp.Activity.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tranthephong.fooddeliveryapp.R

@Composable
fun BottomMenu(
    modifier: Modifier,
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onProfile: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                colorResource(R.color.green),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomMenuItem(icon = painterResource(R.drawable.home), onItemClick = onHomeClick)
        BottomMenuItem(icon = painterResource(R.drawable.favorite), onItemClick = onFavoriteClick)
        BottomMenuItem(icon = painterResource(R.drawable.cart), onItemClick = onCartClick)
        BottomMenuItem(icon = painterResource(R.drawable.profile), onItemClick = onProfile)
    }
}

@Composable
fun BottomMenuItem(
    icon: Painter,
    onItemClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .height(70.dp)
            .clickable { onItemClick?.invoke() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}