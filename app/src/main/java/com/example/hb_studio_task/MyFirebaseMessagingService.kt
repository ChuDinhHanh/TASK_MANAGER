package com.example.hb_studio_task

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_TEST", "Nhận tin từ: ${remoteMessage.from}")
        createNotificationChannel(this)
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val imageUrl = remoteMessage.notification?.imageUrl

        if (imageUrl != null) {
            val loader = ImageLoader(this)
            val request = ImageRequest.Builder(this).data(imageUrl.toString()).target { result ->
                val bitmap = (result as? BitmapDrawable)?.bitmap
                showNotification(title, body, bitmap)
            }.build()
            loader.enqueue(request)
        } else {
            showNotification(title, body, null)
        }
    }

    private fun showNotification(title: String?, message: String?, bitmap: Bitmap?) {
        val channelId = "default_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(title)
            .setContentText(message).setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        bitmap?.let {
            builder.setLargeIcon(it)
            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(it))
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}


private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val soundUri = Uri.parse(
            "android.resource://${context.packageName}/raw/notify_sound"
        )

        val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

        val channel = NotificationChannel(
            "custom_channel", "Custom Notification", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(soundUri, audioAttributes)
            enableVibration(true)
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }
}

