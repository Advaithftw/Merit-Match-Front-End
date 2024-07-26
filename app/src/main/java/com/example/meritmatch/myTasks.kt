package com.example.meritmatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meritmatch.models.Task
import com.example.meritmatch.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MyTasksScreen(token: String, username: String) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(token, username))
    val myTasks by homeViewModel.myTasks.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.Black,
            darkIcons = useDarkIcons
        )
    }

    LaunchedEffect(myTasks) {
        if (myTasks.isNotEmpty()) {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.mytask),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "My Tasks",
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                MyTaskList(tasks = myTasks, onReserveTask = { homeViewModel.reserveTask(it) })
            }
        }
    }
}

@Composable
fun MyTaskList(tasks: List<Task>, onReserveTask: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks) { task ->
            MyTaskItem(task = task, onReserveTask = onReserveTask)
        }
    }
}

@Composable
fun MyTaskItem(task: Task, onReserveTask: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title, style = MaterialTheme.typography.h6)
            Text(task.description)
            Text("Points: ${task.points}")
            Text("Status: ${task.status}")
            Text("Posted By: ${task.postedBy}")
            Button(onClick = { onReserveTask(task.id) }, enabled = task.status == "open") {
                Text("Reserve Task")
            }
        }
    }
}
