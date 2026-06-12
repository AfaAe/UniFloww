package com.example.uniflow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun addTask(task: Task, month: Int, day: Int) {
        val entity = TaskEntity(
            id = task.id,
            textTask = task.textTask,
            isCompleted = task.isCompleted,
            month = month,
            day = day
        )
        taskDao.insert(entity)
    }

    suspend fun updateTask(task: Task, month: Int, day: Int) {
        val entity = TaskEntity(
            id = task.id,
            textTask = task.textTask,
            isCompleted = task.isCompleted,
            month = month,
            day = day
        )
        taskDao.update(entity)
    }

    suspend fun deleteTask(task: Task, month: Int, day: Int) {
        val entity = TaskEntity(
            id = task.id,
            textTask = task.textTask,
            isCompleted = task.isCompleted,
            month = month,
            day = day
        )
        taskDao.delete(entity)
    }

    suspend fun getAllTasks(): List<TaskEntity> {
        return taskDao.getAllTasks()
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAll()
    }
}