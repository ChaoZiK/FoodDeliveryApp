package com.tranthephong.fooddeliveryapp.Activity.Profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tranthephong.fooddeliveryapp.Activity.Home.BottomMenu
import com.tranthephong.fooddeliveryapp.R

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (content, bottomBar) = createRefs()

        // Main content (scrollable or static content)
        Column(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomBar.top)
                }
                .fillMaxWidth()
                .padding(bottom = 16.dp) // give some spacing from bottom bar
        ) {
            // Header background with circle avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE0FFFF)), // Light cyan
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color(0xFFD6F5D6), shape = CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Menu card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    ProfileMenuItem(
                        iconRes = R.drawable.personal_infor,
                        label = "Personal information",
                        onClick = { /* TODO */ }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    ProfileMenuItem(
                        iconRes = R.drawable.orders,
                        label = "Orders",
                        onClick = { /* TODO */ }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    ProfileMenuItem(
                        iconRes = R.drawable.logout,
                        label = "Logout",
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}