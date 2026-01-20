package com.example.hb_studio_task.repository

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import jakarta.inject.Inject

/* #2 - tạo repository bao gồm inject + contructor */
class NotificationRepository @Inject constructor(
    private val fcm: FirebaseMessaging
) {
    fun getFCMToken(onResult: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            onResult(task.result)
        })
    }
}