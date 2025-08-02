package com.tranthephong.fooddeliveryapp.Activity.Authentication

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

private val PrimaryCyan = Color(0xFF2BDAE0)

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    onBackToLogin: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var sendingMail by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Forgot password?",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "No worries, we'll send you reset instructions",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(96.dp))

        Text(
            "Enter your email", fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            placeholder = { Text("Your email") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isBlank()) {
                    Toast.makeText(context, "Please enter your email ", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                sendingMail = true
                FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(email.trim())
                    .addOnCompleteListener { task ->
                        sendingMail = false
                        if (task.isSuccessful) {
                            navController.navigate("resetConfirm") {
                                popUpTo("reset") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                task.exception?.localizedMessage ?: "Something went wrong",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
            shape = RoundedCornerShape(30.dp),
            enabled = !sendingMail,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            if (sendingMail) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Reset Password", fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackToLogin,
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Back to login", color = Color.Black)
        }
    }
}