package com.tranthephong.fooddeliveryapp.Activity.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
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
import com.google.firebase.auth.userProfileChangeRequest
import com.tranthephong.fooddeliveryapp.R
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreen(
                onLoginClick = {
                    startActivity(Intent(this, LoginActivity::class.java))
                },
                onSignUpClick = { name, email, password ->
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val profile = userProfileChangeRequest {
                                    displayName = name
                                }
                                user?.updateProfile(profile)
                                Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Signup Unsuccessful", Toast.LENGTH_SHORT)
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
fun SignUpScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: (name: String, email: String, password: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                            Color(0xFFF4FFFF),
                            Color(0xFFD2FDFF)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(180.dp)
                )
                Text(
                    "Let's sign you up",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
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
                        Text("Name", modifier = Modifier.align(Alignment.Start))
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your name") },
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
                        Text("Email", modifier = Modifier.align(Alignment.Start))
                        TextField(
                            value = email,
                            onValueChange = { email = it },
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
                            onValueChange = { password = it },
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
                        Text("Confirm password", modifier = Modifier.align(Alignment.Start))
                        TextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
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
                        Button(
                            onClick = {
                                when {
                                    email.isBlank() || password.isBlank() -> {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Email or password cannot be blank.")
                                        }
                                    }

                                    password != confirmPassword -> {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Passwords do not match.")
                                        }
                                    }

                                    else -> {
                                        onSignUpClick(name, email, password)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.lightBlue),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Create account", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            buildAnnotatedString {
                                append("Already have an account? ")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                ) {
                                    append("Sign in")
                                }
                            },
                            modifier = Modifier.clickable { onLoginClick() }
                        )
                    }
                }
            }
        }
    }
}