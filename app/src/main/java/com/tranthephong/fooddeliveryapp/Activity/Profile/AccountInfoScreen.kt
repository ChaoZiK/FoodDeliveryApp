package com.tranthephong.fooddeliveryapp.Activity.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tranthephong.fooddeliveryapp.R

@Composable
fun AccountInfoScreen(
    nav: NavController,
    email: String,
    name: String
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { nav.popBackStack() }
                    .size(24.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Account Information",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF0F0F0))
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            InfoRow("Email", email, canClick = false)

            InfoRow("Name", name.takeIf { it.isNotBlank() } ?: "—", canClick = true) {
                nav.navigate("changeName")
            }

            InfoRow("Password", "••••••••", canClick = true) {
                nav.navigate("changePassword")
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, canClick: Boolean, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(enabled = canClick, onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = TextStyle(fontSize = 18.sp)
        )
        Text(
            value,
            color = Color.Gray,
            style = TextStyle(fontSize = 16.sp)
        )
    }
}