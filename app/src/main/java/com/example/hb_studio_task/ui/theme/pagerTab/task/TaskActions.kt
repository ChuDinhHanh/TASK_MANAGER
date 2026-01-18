package com.example.hb_studio_task.ui.theme.pagerTab.task

import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import kotlinx.coroutines.Job

interface TaskActions {
    fun onCompleteTask(taskId: Long, isComp: Boolean, groupId: Long): Job
    fun onTaskClicked(taskId: Long): Unit
    fun onFavorite(taskId: Long, isFav: Boolean): Job

    /* = Unit : đã cung cấp 1 thân hàm rỗng không nhất thiết phải đinh nghĩa lại */
    fun addNewTask(collectionId: Long, content: String) = Unit
    fun addNewTaskToCurrentCollection(content: String) = Unit
    fun updateCurrentCollectionIndex(index: Int) = Unit
    fun addNewCollection(title: String) = Unit
    fun requestAddNewCollection() = Unit
}

