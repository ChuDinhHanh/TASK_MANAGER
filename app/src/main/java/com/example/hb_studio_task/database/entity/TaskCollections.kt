package com.example.hb_studio_task.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_collection")
data class TaskCollections(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collection_id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "update_at")
    val updateAt: Long
)
