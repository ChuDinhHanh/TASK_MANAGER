package com.example.hb_studio_task

import VideoPlayerComponent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hb_studio_task.ui.theme.AndroidTasksTheme
import com.example.hb_studio_task.ui.theme.TasksApp
import com.example.hb_studio_task.ui.theme.component.statusBar.StatusBarProtection


@Composable
fun MainNavigationGraph(mainViewModel: MainViewModel = hiltViewModel()) {/* Main threat */
    val navController = rememberNavController()
    val url by mainViewModel.videoConfig.collectAsState()
    val density = LocalDensity.current
    val statusBarHeightPx = WindowInsets.statusBars.getTop(density).toFloat()
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {
            key(url.url) {
                if (url.url.isNotEmpty()) {
                    VideoPlayerComponent(
                        url = url.url,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        AndroidTasksTheme() {
            NavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                startDestination = NavScreen.HOME.route
            ) {
                composable(NavScreen.HOME.route) { TasksApp(mainViewModel) }
            }

            StatusBarProtection(
                color = Color.Black.copy(alpha = 0.2f), heightProvider = { statusBarHeightPx })
        }
    }

}

enum class NavScreen(val route: String) {
    HOME("home"), TASK("task/{taskId}"), COLLECTION("collection/{collectionId}"), SETTING("setting")
}