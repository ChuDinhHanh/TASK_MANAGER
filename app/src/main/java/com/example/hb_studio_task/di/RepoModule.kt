package com.example.hb_studio_task.di

import com.example.hb_studio_task.database.dao.TaskDAO
import com.example.hb_studio_task.repository.TaskRepo
import com.example.hb_studio_task.repository.TaskRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    // TaskDAO là ở local nên phải tách ra riêng không thể để chung với DB để clean code
    @Singleton
    @Provides
    fun provideTaskRepo(taskDAO: TaskDAO): TaskRepo {
        return TaskRepoImpl(taskDAO)
    }

}