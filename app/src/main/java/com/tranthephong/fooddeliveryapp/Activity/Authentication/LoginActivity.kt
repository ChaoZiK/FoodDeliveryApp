package com.tranthephong.fooddeliveryapp.Activity.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.tranthephong.fooddeliveryapp.Activity.Dashboard.MainActivity
import com.tranthephong.fooddeliveryapp.R
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(
                onSignUpClick = {
                    startActivity(Intent(this, SignUpActivity::class.java))
                },
                onLoginClick = { email, password ->
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: (email: String, password: String) -> Unit
) {
    val (rememberMe, onRememberMeChange) = remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9DFD94), // light green top
                            Color(0xFF939726)  // olive green bottom
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(180.dp)
                )
                Text(
                    "Welcome back",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Email", modifier = Modifier.align(Alignment.Start))
                        TextField(
                            value = email,
                            onValueChange = { email = it},
                            label = { Text("Your email") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF0F0F0),
                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                disabledContainerColor = Color(0xFFF0F0F0),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Password", modifier = Modifier.align(Alignment.Start))
                        TextField(
                            value = password,
                            onValueChange = { password = it},
                            label = { Text("Your password") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF0F0F0),
                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                disabledContainerColor = Color(0xFFF0F0F0),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = rememberMe, onCheckedChange = onRememberMeChange)
                                Text("Remember me")
                            }
                            Text(
                                "Forgot password",
                                modifier = Modifier.clickable { /* Handle forgot password */ })
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                when {
                                    email.isBlank() || password.isBlank() -> {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Please enter email and password.")
                                        }
                                    }

                                    else -> {
                                        onLoginClick(email, password)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00C452), // Background color
                                contentColor = Color.White           // Text/icon color
                            )
                        ) {
                            Text("Login", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            buildAnnotatedString {
                                append("Don't have an account? ")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Blue
                                    )
                                ) {
                                    append("Sign up")
                                }
                            },
                            modifier = Modifier.clickable { onSignUpClick() }
                        )
                    }
                }
            }
        }
    }
}