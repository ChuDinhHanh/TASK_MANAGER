package com.example.hb_studio_task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hb_studio_task.database.dao.TaskDAO
import com.example.hb_studio_task.database.entity.TaskCollections
import com.example.hb_studio_task.database.entity.TaskEntity


private const val DATABASE_NAME = "app.db"
private const val DATABASE_VERSION = 1

@Database(
    entities = [TaskCollections::class, TaskEntity::class],
    version = 1
)

abstract class AppDB : RoomDatabase() {

    abstract fun taskDAO(): TaskDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null
        operator fun invoke(context: Context): AppDB {
            return INSTANCE ?: buidDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buidDatabase(context: Context): AppDB = Room.databaseBuilder(
            context,
            AppDB::class.java,
            DATABASE_NAME
        ).build()
    }
}