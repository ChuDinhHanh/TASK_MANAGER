package com.example.hb_studio_task

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

// Quản lý lifeCycle của ứng dụng
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("TAG", "onCreate")
    }
}