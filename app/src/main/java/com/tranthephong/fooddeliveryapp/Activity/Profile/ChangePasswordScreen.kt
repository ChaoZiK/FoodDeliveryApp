package com.tranthephong.fooddeliveryapp.Activity.Profile

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tranthephong.fooddeliveryapp.R

@Composable
fun ChangePasswordScreen(nav: NavController, email: String) {
    var oldPwd by remember { mutableStateOf("") }
    var newPwd by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var oldPwdVisible by remember { mutableStateOf(false) }
    var newPwdVisible by remember { mutableStateOf(false) }
    var confirmPwdVisible by remember { mutableStateOf(false) }
    val ctx = LocalContext.current

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
                    text = "Password",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Choose a New Password",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Enter and confirm your new password to regain access",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        PasswordField(
            value = oldPwd,
            onValueChange = { oldPwd = it },
            label = "Old password",
            isPasswordVisible = oldPwdVisible,
            onPasswordVisibilityToggle = { oldPwdVisible = !oldPwdVisible }
        )

        Spacer(Modifier.height(16.dp))

        PasswordField(
            value = newPwd,
            onValueChange = { newPwd = it },
            label = "New password",
            isPasswordVisible = newPwdVisible,
            onPasswordVisibilityToggle = { newPwdVisible = !newPwdVisible }
        )

        Spacer(Modifier.height(16.dp))

        PasswordField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Confirm new password",
            isPasswordVisible = confirmPwdVisible,
            onPasswordVisibilityToggle = { confirmPwdVisible = !confirmPwdVisible }
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (newPwd != confirm) {
                    Toast.makeText(ctx, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    val user = FirebaseAuth.getInstance().currentUser
                    val cred = EmailAuthProvider.getCredential(email, oldPwd)
                    user?.reauthenticate(cred)
                        ?.addOnSuccessListener {
                            user.updatePassword(newPwd)
                                .addOnSuccessListener {
                                    Toast.makeText(ctx, "Password updated", Toast.LENGTH_SHORT)
                                        .show()
                                    nav.popBackStack()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(ctx, e.message, Toast.LENGTH_LONG).show()
                                }
                        }
                        ?.addOnFailureListener { e ->
                            Toast.makeText(ctx, "Old password incorrect", Toast.LENGTH_SHORT).show()
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .clip(RoundedCornerShape(200.dp)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00C4F2)),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            Text("Confirm", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (isPasswordVisible) {
                painterResource(id = R.drawable.eye_on)
            } else {
                painterResource(id = R.drawable.eye_off)
            }
            Icon(
                painter = icon,
                contentDescription = "Toggle password visibility",
                modifier = Modifier.clickable { onPasswordVisibilityToggle() }
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color(0xFFF0F0F0),
            focusedBorderColor = Color(0xFF00C4F2),
            unfocusedBorderColor = Color.Gray
        )
    )
}
