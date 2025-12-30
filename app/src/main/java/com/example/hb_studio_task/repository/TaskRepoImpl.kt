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

    override suspend fun getTaskByCollectionId(collectionId: Int): List<TaskEntity> = withContext(
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
                id = id.toInt()
            );
        } else {
            null
        }
    }
}