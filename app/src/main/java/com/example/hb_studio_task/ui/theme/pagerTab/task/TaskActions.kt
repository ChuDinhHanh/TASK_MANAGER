package com.example.hb_studio_task.ui.theme.pagerTab.task

import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState

interface TaskActions {
    fun onCompleteTask(task: TaskUiState): Unit
    fun onTaskClicked(task: TaskUiState): Unit
    fun onFavorite(task: TaskUiState): Unit
    /* = Unit : đã cung cấp 1 thân hàm rỗng không nhất thiết phải đinh nghĩa lại */
    fun addNewTask(collectionId: Long, content: String) = Unit
    fun addNewTaskToCurrentCollection(content: String) = Unit
    fun updateCurrentCollectionIndex(index: Int) = Unit
}

