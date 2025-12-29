package com.example.hb_studio_task.dataStore

import kotlinx.coroutines.flow.Flow

interface AppSetting {
    val appSettingFlow: Flow<AppSettingData>
    suspend fun setIsNotification(isNotification: Boolean)
    suspend fun getIsNotification(): Boolean
}