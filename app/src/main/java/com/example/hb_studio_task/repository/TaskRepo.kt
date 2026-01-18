package com.example.hb_studio_task.repository

import com.example.hb_studio_task.database.entity.TaskCollections
import com.example.hb_studio_task.database.entity.TaskEntity

// Gom các hàm lấy db về chung 1 nguồn gốc tránh việc khó bảo trì!
interface TaskRepo {
    suspend fun getTaskCollection(): List<TaskCollections>
    suspend fun getTaskByCollectionId(collectionId: Long): List<TaskEntity>
    suspend fun addTaskCollection(title: String): TaskCollections?
    suspend fun addTask(content: String, collectionId: Long): TaskEntity?
    /*CRUD*/
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean
    suspend fun updateCollectionCompleted(collectionId: Long, isCompleted: Boolean): Boolean
}
