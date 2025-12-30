package com.example.hb_studio_task

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hb_studio_task.repository.TaskRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Step 1: config hilt + dagger
@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo,
    private val taskRepo2: TaskRepo
) : ViewModel() {
    init {
        Log.d("TAG2", "ViewModel init $taskRepo - $taskRepo2")
    }
}