package com.example.todolistcompose.ui.theme.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class TaskViewModel: ViewModel() {
    private val _tasks = mutableStateListOf<String>()
    val tasks: SnapshotStateList<String> = _tasks

    fun addTask (task: String){
        tasks.add(task)
    }
}