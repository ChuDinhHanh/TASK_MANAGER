package com.example.hb_studio_task.ui.theme.pagerTab.task

import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState

interface TaskActions {
    fun onCompleteTask(task: TaskUiState)
    fun onTaskClicked(task: TaskUiState)
    fun onFavorite(task: TaskUiState)
}