package com.tranthephong.fooddeliveryapp.Activity.Profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.tranthephong.fooddeliveryapp.R

@Composable
fun ChangeNameScreen(nav: NavController, currentName: String) {
    var newName by remember { mutableStateOf("") }
    val ctx = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
                    text = "Name",
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
            NameInfoRow("Current name", currentName)
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "Change name",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Black,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            label = { Text("Enter new name") },
            colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color(0xFFF3F3F3),
                focusedBorderColor = Color(0xFF00C4F2),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                val user = FirebaseAuth.getInstance().currentUser
                if (newName.isBlank()) {
                    Toast.makeText(ctx, "Name can’t be blank", Toast.LENGTH_SHORT).show()
                } else {
                    user?.updateProfile(
                        userProfileChangeRequest { displayName = newName }
                    )?.addOnSuccessListener {
                        Toast.makeText(ctx, "Name updated", Toast.LENGTH_SHORT).show()
                        nav.popBackStack()
                    }?.addOnFailureListener {
                        Toast.makeText(ctx, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .clip(RoundedCornerShape(200.dp)),
            contentPadding = PaddingValues(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF00C4F2)
            )
        ) {
            Text("Confirm", fontSize = 20.sp, color = Color.White)
        }
    }
}

@Composable
fun NameInfoRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFF0F0F0)),
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