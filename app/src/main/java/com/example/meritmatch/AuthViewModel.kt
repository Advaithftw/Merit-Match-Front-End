package com.example.meritmatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meritmatch.models.AuthRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _signupResult = MutableStateFlow<String?>(null)
    val signupResult: StateFlow<String?> = _signupResult

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    val _username = MutableStateFlow("")


    var password = MutableStateFlow("")
    private val _points = MutableStateFlow(0)
    val points: StateFlow<Int> = _points


    fun signup() = viewModelScope.launch {
        try {
            val response = apiService.signup(AuthRequest(_username.value, password.value))
            if (response.isSuccessful) {
                response.body()?.token?.let {
                    _token.value = it
                    _points.value=100
                    _signupResult.value = "Signup successful"
                }
            } else {
                _signupResult.value = "User already exists"
            }
        } catch (e: Exception) {
            _signupResult.value = "Error: ${e.message}"
        }
    }

    fun login() = viewModelScope.launch {
        try {
            val response = apiService.login(AuthRequest(_username.value, password.value))
            if (response.isSuccessful) {
                response.body()?.token?.let {
                    _token.value = it
                    _loginResult.value = "Login successful"
                }
            } else {
                _loginResult.value = "Invalid username or password"
            }
        } catch (e: Exception) {
            _loginResult.value = "Error: ${e.message}"
        }
    }
}
