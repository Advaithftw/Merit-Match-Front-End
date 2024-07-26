package com.example.meritmatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meritmatch.viewmodels.HomeViewModel

class HomeViewModelFactory(private val token: String,private val username: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(token, username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
