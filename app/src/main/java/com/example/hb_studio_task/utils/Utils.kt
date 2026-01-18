package com.example.hb_studio_task.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hb_studio_task.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun rememberRelativeTime(
    timeInMillis: Long, updateInterval: Long = 60_000L // 1 phút
): String {/* produceState: Vẽ UI */
    val now by produceState(
        initialValue = System.currentTimeMillis(),/* Khi key1 thay đổi thì nó sẽ bị cancel và chạy lại cái coroutine mới */
        key1 = timeInMillis
    ) {
        while (true) {/* Khi value thay đổi thì nó sẽ chạy lại */
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


fun vibrateSuccess(context: Context, durationMillis: Long = 10) {
    // Get the Vibrator service
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Use VibratorManager for API 31 (Android 12) and above
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        // Fallback for devices below API 31
        @Suppress("DEPRECATION") context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    // Vibrate the device
    // For API 26 (Android O) and above
    val effect = VibrationEffect.createOneShot(20, 40)
    vibrator.vibrate(effect)
}

@Composable
fun FireworkTrigger(
    composition: LottieComposition?,
    onAnimatedFinish: () -> Unit
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.firework_lottie)
    )
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        if (composition != null) {
            launch {
                // reset về frame đầu tiên
                lottieAnimatable.snapTo(composition, 0f)
                // Chạy đến khi kết thúc
                lottieAnimatable.animate(composition, 1)
                onAnimatedFinish()
            }
        }
    }
    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = { lottieAnimatable.progress }, // Lấy tiến trình từ animatable
            modifier = Modifier.fillMaxSize(),
            dynamicProperties = null
        )
    }

}