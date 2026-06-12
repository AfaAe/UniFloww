package com.example.uniflow.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uniflow.TaskRepository

class ViewModelFact(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelTask::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelTask(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}