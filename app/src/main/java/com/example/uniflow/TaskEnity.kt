package com.example.uniflow

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val textTask: String,
    val isCompleted: Boolean,
    val month: Int,
    val day: Int
)