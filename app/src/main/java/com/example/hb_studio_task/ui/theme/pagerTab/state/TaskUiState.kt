package com.example.hb_studio_task.ui.theme.pagerTab.state

import com.example.hb_studio_task.database.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TaskUiState(
    val id: Long,
    val content: String,
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false,
    val collectionId: Long,
    val updatedAt: Long,
    val stringUpdatedAt: String,
    val createdAt: Long
)
