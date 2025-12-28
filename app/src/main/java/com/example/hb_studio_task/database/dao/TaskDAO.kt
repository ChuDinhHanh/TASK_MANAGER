package com.example.hb_studio_task.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hb_studio_task.database.entity.TaskCollections
import com.example.hb_studio_task.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface TaskDAO {
    // C
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskCollections: TaskCollections)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    // R
    @Query("SELECT * FROM task_collection")
    suspend fun getTaskCollections(): List<TaskCollections>

    @Query("SELECT * FROM task WHERE collection_id = :collectionId")
    suspend fun getTask(collectionId: Int): TaskCollections

    // U
    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskIsFavorite(taskId: Int, isFavorite: Boolean)

    @Update
    suspend fun updateTaskCollection(taskCollections: TaskCollections)

    // D
    @Delete
    suspend fun deleteTaskCollection(taskCollections: TaskCollections)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

}