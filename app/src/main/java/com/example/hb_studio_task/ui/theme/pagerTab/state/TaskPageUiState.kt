package com.example.hb_studio_task.ui.theme.pagerTab.state

/* Có 2 trạng thái */
data class TaskPageUiState(
    val activeTaskList: List<TaskUiState>,
    val completedTaskList: List<TaskUiState>
)
