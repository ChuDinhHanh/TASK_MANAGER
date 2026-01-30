package com.example.hb_studio_task.repository

import com.example.hb_studio_task.database.dao.TaskDAO
import com.example.hb_studio_task.database.entity.TaskCollections
import com.example.hb_studio_task.database.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class TaskRepoImpl(private val taskDAO: TaskDAO) : TaskRepo {

    override suspend fun getTaskCollection(): List<TaskCollections> = withContext(Dispatchers.IO) {
        taskDAO.getTaskCollections()
    }

    override suspend fun getTaskByCollectionId(collectionId: Long): List<TaskEntity> = withContext(
        Dispatchers.IO
    ) {
        taskDAO.getTask(collectionId)
    }

    override suspend fun addTaskCollection(title: String): TaskCollections? {
        val taskCollection =
            TaskCollections(title = title, updateAt = Calendar.getInstance().timeInMillis);
        val id = taskDAO.insertTaskCollections(taskCollection);
        return if (id > 0) {
            taskCollection.copy(
                id = id
            );
        } else {
            null
        }
    }

    override suspend fun addTask(
        content: String, collectionId: Long
    ): TaskEntity? = withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val newTask = TaskEntity(
            content = content,
            isFavorite = false,
            isCompleted = false,
            createdAt = now,
            updatedAt = now,
            images = null,    // Nếu chưa có ảnh thì để null
            documents = null, // Nếu chưa có tài liệu thì để null
            collectionId = collectionId  // Giả sử id của TaskCollection cha là 1
        )
        val id = taskDAO.insertTaskEntity(newTask)
        if (id > 0) {
            newTask.copy(id = id)
        } else {
            null
        }
    }

    override suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            taskDAO.updateTaskIsFavorite(taskId, isFavorite) > 0
        }

    override suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            taskDAO.updateTaskCompleted(taskId, isCompleted) > 0
        }

    override suspend fun updateCollectionCompleted(
        collectionId: Long, isCompleted: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        taskDAO.updateCollectionCompleted(collectionId, isCompleted) > 0
    }

    override suspend fun deleteCollectionById(collectionId: Long): Boolean =
        withContext(Dispatchers.IO) {
            taskDAO.deleteTaskCollectionById(collectionId) > 0
        }
}