package com.example.todolistcompose.ui.theme.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {

    var isAdding by remember { mutableStateOf(false) }
    var newTask by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime

    fun clearTask(){
        viewModel.addTask(newTask.trim())
        newTask = ""
        isAdding = false
        keyboardController?.hide()
        focusManager.clearFocus()
    }
    LaunchedEffect(isAdding) {
        if (isAdding) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }


    LaunchedEffect(Unit) {
        snapshotFlow { imeInsets.getBottom(density) }
            .distinctUntilChanged()
            .collect { imeBottom ->
                if (imeBottom == 0) {
                    isAdding = false
                }
            }
    }
    BackHandler(enabled = isAdding) {
        isAdding = false
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Мои задачи") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (isAdding) clearTask() else isAdding = true
            }, containerColor = Color.Blue, contentColor = Color.White) {
                Icon(
                    imageVector = if (isAdding) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null
                )
            }

        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(viewModel.tasks) { task ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                ) {
                                    Text(task, modifier = Modifier.padding(16.dp))
                                }
                            }
                }
            }
            if(isAdding){
                TextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    placeholder = { Text("Новая задача") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 88.dp, bottom = 16.dp)
                        .imePadding()
                        .focusRequester(focusRequester)
                        .align(Alignment.BottomCenter),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { clearTask() }
                    )
                )
            }
        }
    }
}