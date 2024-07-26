package com.example.meritmatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meritmatch.ApiService
import com.example.meritmatch.RetrofitInstance
import com.example.meritmatch.models.Task
import com.example.meritmatch.models.TaskRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val token: String, private val username: String) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _myTasks = MutableStateFlow<List<Task>>(emptyList())
    val myTasks: StateFlow<List<Task>> = _myTasks

    private val _points = MutableStateFlow(0)
    val points: StateFlow<Int> = _points

    private val apiService: ApiService = RetrofitInstance.api

    init {
        fetchTasks()
        fetchMyTasks()
        fetchPoints()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            val response = apiService.getTasks("Bearer $token")
            if (response.isSuccessful) {
                val allTasks = response.body() ?: emptyList()
                val sortedTasks = allTasks.filter { it.postedBy != username }
                _tasks.value = sortedTasks
            }
        }
    }

    private fun fetchMyTasks() {
        viewModelScope.launch {
            try {
                val response = apiService.getMyTasks("Bearer $token")
                if (response.isSuccessful) {
                    _myTasks.value = response.body() ?: emptyList()
                } else {
                    Log.e("fetchMyTasks", "Error: ${response.errorBody()?.string()}")
                    _myTasks.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("fetchMyTasks", "Exception: ${e.message}")
                _myTasks.value = emptyList()
            }
        }
    }

    private fun fetchPoints() {
        viewModelScope.launch {
            try {
                val response = apiService.getUserPoints("Bearer $token", username)
                if (response.isSuccessful) {
                    val userPointsResponse = response.body()
                    _points.value = userPointsResponse?.karmaPoints ?: 0
                } else {
                    _points.value = 0
                    Log.e("fetchPoints", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // Handle exception
                _points.value = 0
                Log.e("fetchPoints", "Exception: ${e.message}")
            }
        }
    }


    fun createTask(title: String, description: String, points: Int, username: String) {
        viewModelScope.launch {
            val request = TaskRequest(title, description, points, username)
            val response = apiService.createTask("Bearer $token", request)
            if (response.isSuccessful) {
                fetchTasks()
                fetchMyTasks()
                fetchPoints()
            }
        }
    }

    fun reserveTask(taskId: String) {
        viewModelScope.launch {
            val response = apiService.reserveTask("Bearer $token", taskId)
            if (response.isSuccessful) {
                fetchTasks()
                fetchMyTasks()
                fetchPoints()
            }
            val updatedTasks = _tasks.value.map { task ->
                if (task.id == taskId) {
                    task.copy(status = "reserved", reservedBy = username)
                } else {
                    task
                }
            }
            _tasks.value = updatedTasks


        }
    }
}
