package com.example.meritmatch.models

data class User(val id: String, val username: String, val karmaPoints: Int)

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val points: Int,
    val status: String,
    val postedBy: String,
    val reservedBy: String?
)

data class Id(
    val `$oid`: String
)
data class UserPointsResponse(
    val karmaPoints: Int
)

data class AuthResponse(val token: String)
data class TaskRequest(val title: String, val description: String, val points: Int,val postedBy: String)
data class AuthRequest(val username: String, val password: String)
