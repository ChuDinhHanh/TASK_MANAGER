package com.example.hb_studio_task.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_collection")
data class TaskCollections(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collection_id")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long? = System.currentTimeMillis(),
    @ColumnInfo(name = "update_at")
    val updateAt: Long,
    @ColumnInfo(name = "had_finish")
    val hadFinish: Boolean = false
)

enum class SortType(val value: Int) {
    CREATED_DATE(0),
    FAVORITE(1),
}

fun Int.toSortType(): SortType {
    return when (this) {
        1 -> SortType.FAVORITE
        else -> SortType.CREATED_DATE
    }
}
