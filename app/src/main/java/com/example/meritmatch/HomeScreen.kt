package com.example.meritmatch.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.meritmatch.HomeViewModelFactory
import com.example.meritmatch.R
import com.example.meritmatch.models.Task
import com.example.meritmatch.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeScreen(token: String, username: String, navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(token, username))
    val tasks by homeViewModel.tasks.collectAsState()
    val points by homeViewModel.points.collectAsState(initial = 0)
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.Black,
            darkIcons = useDarkIcons
        )
    }

    LaunchedEffect(tasks) {
        if (tasks.isNotEmpty()) {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.screen),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopBar(username = username, points = points, onLogout = {
                navController.navigate("login")
            })
            TaskForm(
                username = username,
                onCreateTask = { title, description, points ->
                    homeViewModel.createTask(title, description, points, username)
                    Toast.makeText(context, "Task created successfully!", Toast.LENGTH_SHORT).show()
                },
                onMyTasksClicked = { navController.navigate("mytasks/$token/$username") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                TaskList(tasks = tasks, onReserveTask = { homeViewModel.reserveTask(it) }, currentUser = username)
            }
        }
    }
}




@Composable
fun TopBar(username: String, points: Int, onLogout: () -> Unit) {
    TopAppBar(
        title = {
            Text("$username's Helpdesk")
        },
        actions = {
            Text("Points: $points")
        },
        navigationIcon = {
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White // Set the desired color here
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TaskForm(username: String, onCreateTask: (String, String, Int) -> Unit, onMyTasksClicked: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = points,
            onValueChange = { points = it },
            label = { Text("Points") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                onCreateTask(title, description, points.toInt())
                title = ""
                description = ""
                points = ""
            }) {
                Text("Create Task")
            }
            Button(onClick = { onMyTasksClicked() }) {
                Text("My Tasks")
            }
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, onReserveTask: (String) -> Unit, currentUser: String) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks) { task ->
            TaskItem(task = task, onReserveTask = onReserveTask, currentUser = currentUser)
        }
    }
}



@Composable
fun TaskItem(task: Task, onReserveTask: (String) -> Unit, currentUser: String) {
    var isReserved by remember(task.status) { mutableStateOf(task.status != "pending") }
    var buttonText by remember(task.status) { mutableStateOf(if (isReserved) "Waiting" else "Reserve Task") }
    var buttonEnabled by remember(task.status) { mutableStateOf(task.status == "pending" && task.postedBy != currentUser) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title ?: "", style = MaterialTheme.typography.h6)
            Text(task.description ?: "")
            Text("Points: ${task.points ?: 0}")
            Text("Status: ${task.status ?: "unknown"}")
            Text("Posted By: ${task.postedBy ?: "unknown"}")

            Button(
                onClick = {
                    if (!isReserved) {
                        task.id?.let {
                            onReserveTask(it)
                        }
                        isReserved = true
                        buttonText = "Waiting"
                        buttonEnabled = false
                    }
                },
                enabled = buttonEnabled,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (buttonEnabled) MaterialTheme.colors.primary else Color.Gray
                )
            ) {
                Text(buttonText)
            }
        }
    }
}





