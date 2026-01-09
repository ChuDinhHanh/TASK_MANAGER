package com.example.hb_studio_task.ui.theme.pagerTab.state

import com.example.hb_studio_task.database.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.Long

data class TaskUiState(
    val id: Long,
    val content: String,
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false,
    val collectionId: Long,
    val updatedAt: Long,
    val stringUpdatedAt: String,
    val createdAt: Long = System.currentTimeMillis()
)

fun TaskEntity.toTaskUiState(): TaskUiState {
    return TaskUiState(
        id = this.id,
        content = this.content,
        isFavorite = this.isFavorite,
        isCompleted = this.isCompleted,
        collectionId = this.collectionId,
        updatedAt = this.updatedAt,
        stringUpdatedAt = this.updatedAt.millisToDateString(),
        createdAt = this.createdAt
    )
}

fun Long.millisToDateString(): String {
    return SimpleDateFormat("EEE,dd MMM yyyy", Locale.getDefault()).format(Date(this)).toString()
}
