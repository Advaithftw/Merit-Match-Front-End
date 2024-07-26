package com.example.meritmatch

import com.example.meritmatch.models.AuthRequest
import com.example.meritmatch.models.AuthResponse
import com.example.meritmatch.models.Task
import com.example.meritmatch.models.TaskRequest
import com.example.meritmatch.models.UserPointsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/signup")
    suspend fun signup(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @GET("/tasks")
    suspend fun getTasks(@Header("Authorization") token: String): Response<List<Task>>

    @POST("/tasks")
    suspend fun createTask(@Header("Authorization") token: String, @Body request: TaskRequest): Response<Task>

    @GET("/mytasks")
    suspend fun getMyTasks(@Header("Authorization") token: String): Response<List<Task>>

    @PATCH("/api/tasks/{taskId}/reserve")
    suspend fun reserveTask(@Header("Authorization") token: String, @Path("taskId") taskId: String): Response<Task>

    @PATCH("/api/tasks/{taskId}/complete")
    suspend fun completeTask(@Header("Authorization") token: String, @Path("taskId") taskId: String): Response<Task>

    @GET("/{username}/points")
    suspend fun getUserPoints(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<UserPointsResponse>
}

