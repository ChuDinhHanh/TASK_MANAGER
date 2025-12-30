package com.example.hb_studio_task.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.hb_studio_task.dataStore.AppSettingDataStoreKey.IS_NOTIFICATION_ON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Jetpack DataStore (Preferences). là phương án thay thể của sharePreferency
class AppSettingImpl(private val context: Context) : AppSetting {

    private val Context.dataStoreAppSetting: DataStore<Preferences> by
    preferencesDataStore(name = "app-setting-pref")

    //    Cho phép đọc gía trị realtime nănhf flow và emit ra
    override val appSettingFlow: Flow<AppSettingData>
        get() = context.dataStoreAppSetting.data.map { pref ->
            AppSettingData(
                isNotificationOn = pref[IS_NOTIFICATION_ON] ?: false
            )
        }

    //    Làm việc với đọc ghi là phải đỏio luồng sao IO
    override suspend fun setIsNotification(isNotification: Boolean) = withContext(Dispatchers.IO) {
        context.dataStoreAppSetting.edit { pref ->
            pref[AppSettingDataStoreKey.IS_NOTIFICATION_ON] = isNotification
        }
//        Sử dụng Unit vì ta sử dụng dấu = nên ta phải có Unit
        Unit
    }

    override suspend fun getIsNotification(): Boolean = withContext(Dispatchers.IO) {
        // Cach 1
        /*
        * context.dataStoreAppSetting.data.map { pref ->
            pref[AppSettingDataStoreKey.IS_NOTIFICATION_ON] ?: false
        }.first()
        *
        */

        // Cach 2
        /**
         *   context.dataStoreAppSetting.data.map { pref ->
         *   it[AppSettingDataStoreKey.IS_NOTIFICATION_ON] ?: false
         *   }.first()
         */
        appSettingFlow.first().isNotificationOn
    }
}