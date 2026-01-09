package com.example.hb_studio_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hb_studio_task.ui.theme.home.HomeLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /* viewModels = Android tự tạo & giữ ViewModel cho Activity theo lifecycle*/
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val taskDelegate = remember { mainViewModel }
            val listTaskGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle()
            HomeLayout(listTaskGroup, taskDelegate)
        }
    }
}

