package com.tranthephong.fooddeliveryapp.Activity.Splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.tranthephong.fooddeliveryapp.Activity.Authentication.LoginActivity
import com.tranthephong.fooddeliveryapp.Activity.Authentication.SignUpActivity
import com.tranthephong.fooddeliveryapp.Activity.BaseActivity
import com.tranthephong.fooddeliveryapp.MainActivity
import com.tranthephong.fooddeliveryapp.R
import com.tranthephong.fooddeliveryapp.Util.RememberPrefs


class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!RememberPrefs.isFirstLaunch(this)) {
            routeUser()
            return
        }

        setContent {
            SplashScreen(
                onSignUpClick = {
                    RememberPrefs.setOnboardingDone(this)
                    startActivity(Intent(this, SignUpActivity::class.java))
                },
                onLoginClick = {
                    RememberPrefs.setOnboardingDone(this)
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            )
        }
    }

    private fun routeUser() {
        val target = if (
            RememberPrefs.shouldAutoLogin(this) && FirebaseAuth.getInstance().currentUser != null
        ) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(Intent(this, target))
        finish()
    }
}

@Preview
@Composable
fun SplashScreen(
    onSignUpClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.backgroundBlue))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.splash_food),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Make Your Own Food\nStay at Home",
            color = colorResource(R.color.strongBlue),
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp,
            modifier = Modifier.padding(top = 32.dp)
        )

        Text(
            text = "Select your preferred dish from our app and enjoy a delightful culinary experience today!",
            color = colorResource(R.color.black),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            lineHeight = 30.sp,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 32.dp)
                .padding(horizontal = 16.dp)
        )

        Button(
            onClick = onSignUpClick,
            shape = RoundedCornerShape(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.lightBlue)
            ),
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Button(
            onClick = onLoginClick,
            shape = RoundedCornerShape(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.white)
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}