package com.example.todolistcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolistcompose.ui.theme.TodoListComposeTheme
import com.example.todolistcompose.ui.theme.screens.TaskListScreen
import com.example.todolistcompose.ui.theme.screens.TaskViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        setContent {
            TodoListComposeTheme {
                val navController =  rememberNavController()
                val taskViewModel: TaskViewModel = viewModel<TaskViewModel>()
                NavHost(navController, startDestination = "list"){
                    composable("list") {
                        TaskListScreen(
                            viewModel = taskViewModel,
                        )
                    }
                }
            }
        }
    }
}