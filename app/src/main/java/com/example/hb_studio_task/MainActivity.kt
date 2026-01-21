package com.example.hb_studio_task

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.hb_studio_task.ui.theme.component.statusBar.StatusBarProtection
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /* viewModels = Android tự tạo & giữ ViewModel cho Activity theo lifecycle*/
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        /* setKeepOnScreenCondition~ auto check*/
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isReady.value
        }
        enableEdgeToEdge()
        firebaseAnalytics = Firebase.analytics
        mainViewModel.fetchAndSaveToken()
        requestNotificationPermissionIfNeeded()
        setContent {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {}
            MainNavigationGraph()
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001
                )
            }
        }
    }
}

