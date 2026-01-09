package com.example.hb_studio_task.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


    @Composable
    fun rememberRelativeTime(
        timeInMillis: Long,
        updateInterval: Long = 60_000L // 1 phút
    ): String {
        /* produceState: Vẽ UI */
        val now by produceState(
            initialValue = System.currentTimeMillis(),
            /* Khi key1 thay đổi thì nó sẽ bị cancel và chạy lại cái coroutine mới */
            key1 = timeInMillis
        ) {
            while (true) {
                /* Khi value thay đổi thì nó sẽ chạy lại */
                value = System.currentTimeMillis()
                delay(updateInterval)
            }
        }

        return getRelativeTime(timeInMillis, now)
    }


fun getRelativeTime(timeInMillis: Long, now: Long): String {
    val diff = System.currentTimeMillis() - timeInMillis

    val minutes = diff / (60 * 1000)
    val hours = diff / (60 * 60 * 1000)
    val days = diff / (24 * 60 * 60 * 1000)

    return when {
        diff < 60_000 -> "vừa xong"
        minutes < 60 -> "$minutes phút trước"
        hours < 24 -> "$hours giờ trước"
        days < 30 -> "$days ngày trước"
        else -> formatMillis(timeInMillis, "yyyy-MM-dd HH:mm:ss")
    }
}

fun formatMillis(milliseconds: Long, pattern: String): String {
    val instant = Instant.ofEpochMilli(milliseconds)

    val formatter = DateTimeFormatter.ofPattern(pattern)
        .withZone(ZoneId.systemDefault()) // Use the system's default time zone

    return formatter.format(instant)
}