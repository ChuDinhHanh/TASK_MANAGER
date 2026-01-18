package com.example.hb_studio_task.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hb_studio_task.database.entity.TaskCollections
import com.example.hb_studio_task.database.entity.TaskEntity

@Dao
interface TaskDAO {
    // C
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCollections(taskCollections: TaskCollections): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskEntity(task: TaskEntity): Long

    // R
    @Query("SELECT * FROM task_collection")
    suspend fun getTaskCollections(): List<TaskCollections>

    @Query("UPDATE task_collection SET had_finish =:isCompleted  WHERE collection_id = :collectionId")
    suspend fun updateCollectionCompleted(collectionId: Long, isCompleted: Boolean): Int

    @Query("SELECT * FROM task WHERE collection_id = :collectionId")
    suspend fun getTask(collectionId: Long): List<TaskEntity>

    // U
    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskIsFavorite(taskId: Long, isFavorite: Boolean): Int

    @Update
    suspend fun updateTaskCollection(taskCollections: TaskCollections)

    @Query("UPDATE task SET is_completed = :isCompleted, updated_at = :updatedAt WHERE id = :taskId")
    suspend fun updateTaskCompleted(
        taskId: Long, isCompleted: Boolean, updatedAt: Long = System.currentTimeMillis()
    ): Int

    // D
    @Delete
    suspend fun deleteTaskCollection(taskCollections: TaskCollections)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

}