package com.example.meritmatch.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meritmatch.AuthViewModel
import com.example.meritmatch.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoginPage(authViewModel: AuthViewModel = viewModel(), onAuthSuccess: (String, String) -> Unit) {
    // Collect state flows inside composable functions
    val token by authViewModel.token.collectAsState()
    val signupResult by authViewModel.signupResult.collectAsState()
    val loginResult by authViewModel.loginResult.collectAsState()
    val username by authViewModel._username.collectAsState()
    val password by authViewModel.password.collectAsState()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.Black,
            darkIcons = useDarkIcons
        )
    }


    LaunchedEffect(token) {
        token?.let {
            onAuthSuccess(it, username)
        }
    }


    LaunchedEffect(signupResult) {
        signupResult?.let {
            val message = if (it == "Signup successful") "Signup successful. You have been awarded 100 points!" else "Signup failed: $it"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


    LaunchedEffect(loginResult) {
        loginResult?.let {
            val message = if (it == "Login successful") "Welcome back, $username!" else "Login failed: $it"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.hompage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Merit Match",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = username,
                    onValueChange = { authViewModel._username.value = it },
                    label = { Text("Username") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.LightGray,
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { authViewModel.password.value = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.LightGray,
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { authViewModel.signup() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { authViewModel.login() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF2196F3),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log In")
                }
            }
        }
    }
}
