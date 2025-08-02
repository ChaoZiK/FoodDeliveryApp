package com.tranthephong.fooddeliveryapp.Activity.Cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranthephong.fooddeliveryapp.R

@Composable
fun ConfirmationScreen(
    onBack: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(R.drawable.back_arrow),
            contentDescription = "Back",
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .clickable(onClick = onBack)
        )

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(120.dp)
                    .background(Color(0xFF00E53D), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.tick),
                    contentDescription = "Success",
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Order Confirmation",
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Your order is confirmed! Processing now,",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 32.dp),
            )
            Text(
                text = "shipping will follow soon.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 32.dp),
            )
        }
    }
}