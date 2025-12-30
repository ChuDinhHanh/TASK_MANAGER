package com.example.hb_studio_task.di

import android.content.Context
import com.example.hb_studio_task.database.AppDB
import com.example.hb_studio_task.database.dao.TaskDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
// LifeCycle : tạo ra mà không thể bị thay thế!
@InstallIn(SingletonComponent::class)
// Nơi cung cấp object cần thiết để truyền vô dependency
object DatabaseModule {
    @Singleton
    @Provides
    fun provideTaskDAO(appDb: AppDB): TaskDAO {
        return appDb.taskDAO()
    }

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDB {
        return AppDB.invoke(context)
    }
}