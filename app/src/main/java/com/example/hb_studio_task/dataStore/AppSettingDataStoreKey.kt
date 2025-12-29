package com.example.hb_studio_task.dataStore

import androidx.datastore.preferences.core.booleanPreferencesKey

object AppSettingDataStoreKey {
    val IS_NOTIFICATION_ON = booleanPreferencesKey("is_notification_on")
}