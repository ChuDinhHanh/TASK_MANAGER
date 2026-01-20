package com.example.hb_studio_task.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import jakarta.inject.Inject

data class VideoConfig(
    val url: String = "",
    val isEnabled: Boolean = false
)

class ConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    fun getAppTitle(onResult: (String) -> Unit) {
        // Fetch dữ liệu mới nhất từ server
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Lấy giá trị với key "app_title" đã đặt trên Firebase Console
                val title = remoteConfig.getString("Title")
                onResult(title)
            } else {
                // Nếu lỗi (ví dụ mất mạng), lấy giá trị mặc định hoặc hiện tại
                onResult(remoteConfig.getString("Title"))
            }
        }
    }


    fun getVideoConfig(onResult: (VideoConfig) -> Unit) {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val config = VideoConfig(
                    url = remoteConfig.getString("promo_video_url"),
                    isEnabled = remoteConfig.getBoolean("show_promo_video")
                )
                onResult(config)
            }
        }
    }
}