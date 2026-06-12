package com.example.uniflow.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniflow.Task
import com.example.uniflow.TaskEntity
import com.example.uniflow.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelTask(
    private val repository: TaskRepository
) : ViewModel() {

    suspend fun getAllTasks(): List<TaskEntity> {
        return withContext(Dispatchers.IO) {
            repository.getAllTasks()
        }
    }

    fun addTask(task: Task, month: Int, day: Int) {
        viewModelScope.launch {
            try {
                repository.addTask(task, month, day)
            } catch (e: Exception) {
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            try {
                repository.deleteAllTasks()
            } catch (e: Exception) {
            }
        }
    }
}