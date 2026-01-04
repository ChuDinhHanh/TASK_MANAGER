package com.example.hb_studio_task.ui.theme.pagerTab.task

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor() : ViewModel(), TaskActions {
    override fun onCompleteTask(task: TaskUiState) {
        Log.d("TAG","onCompleteTask")
    }

    override fun onTaskClicked(task: TaskUiState) {
        Log.d("TAG","onTaskClicked")
    }

    override fun onFavorite(task: TaskUiState) {
        Log.d("TAG","onFavorite")
    }

}