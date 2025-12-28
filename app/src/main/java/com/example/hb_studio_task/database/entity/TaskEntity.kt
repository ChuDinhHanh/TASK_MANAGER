package com.example.hb_studio_task.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity tượng trưng cho 1 table trong database room

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "title")
    val content: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "update_at")
    val updatedAt: Long,
    @ColumnInfo(name = "is_images")
    val images: Int?,
    @ColumnInfo(name = "is_documents")
    val documents: Int?,
//    Liên kết với bảng cha của nó là TaskCollection
    @ColumnInfo(name = "collection_id")
    val collectionId: Int
)