package com.example.hb_studio_task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hb_studio_task.dataStore.AppConstants
import com.example.hb_studio_task.database.dao.TaskDAO
import com.example.hb_studio_task.database.entity.TaskCollections
import com.example.hb_studio_task.database.entity.TaskEntity


@Database(
    entities = [TaskCollections::class, TaskEntity::class], version = AppConstants.DATABASE_VERSION
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
            context, AppDB::class.java, AppConstants.DATABASE_NAME
        ).addMigrations(MIGRATE_1_2).build()
    }

}

private val MIGRATE_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL()
    }
}