package com.example.hb_studio_task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hb_studio_task.repository.TaskRepo
import com.example.hb_studio_task.ui.theme.pagerTab.state.TabUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskGroupUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.toTaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Step 1: config hilt + dagger
@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
) : ViewModel(), TaskActions {
    /* MutableStateFlow: HotFlow */
    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> =
        MutableStateFlow(emptyList());

    /* asStateFlow = 🔒 Ẩn khả năng ghi – chỉ cho đọc
    1 Nguyên tắc đóng gói bên trong model có thể can thiệp nhưng bên ngoài chỉ được đocj thôi
    >> Read only bên ngoài chỉ được observe thôi oke chưa
    2 Kotlin mặc định val không ghi modifier = public
    */
    val listTabGroup = _listTabGroup.asStateFlow()

    private var _currentSelectedCollectionIndex = 0;

    init {
        val task1 = TaskUiState(
            id = 1L,
            content = "Học lập trình Kotlin 1",
            collectionId = 101L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            stringUpdatedAt = "15:30 - 20/05/2024"
        )

        val task2 = TaskUiState(
            id = 2L,
            content = "Học lập trình Kotlin 2",
            collectionId = 101L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            stringUpdatedAt = "15:30 - 20/05/2024"
        )

        val task3 = TaskUiState(
            id = 3L,
            content = "Học lập trình Kotlin 2",
            collectionId = 101L,
            isCompleted = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            stringUpdatedAt = "15:30 - 20/05/2024"
        )

        _listTabGroup.value = listOf<TaskGroupUiState>(
            TaskGroupUiState(
                TabUiState(
                    id = 1,
                    title = "Tab-1"
                ), TaskPageUiState(
                    activeTaskList = listOf<TaskUiState>(task1, task2),
                    completedTaskList = listOf<TaskUiState>(task3)
                )
            ),
            TaskGroupUiState(
                TabUiState(
                    id = 1,
                    title = "Tab-2"
                ), TaskPageUiState(
                    activeTaskList = listOf(),
                    completedTaskList = listOf()
                )
            ),
        )

    }


    fun invertTaskFavorite(taskUiState: TaskUiState) = viewModelScope.launch(Dispatchers.IO) {
        val newTaskUiState = taskUiState.copy(isCompleted = !taskUiState.isCompleted)
        _listTabGroup.value.let { tabs ->
            val newTabGroup = tabs.map { it ->
                val finalList = it.page.activeTaskList + it.page.completedTaskList
                val updateList = finalList.map { task ->
                    if (task.id == newTaskUiState.id) newTaskUiState.copy() else task
                }

                val newPage = it.page.copy(
                    activeTaskList = updateList.filter {
                        !it.isCompleted
                    },
                    completedTaskList = updateList.filter { it.isCompleted }
                )
                it.copy(page = newPage)
            }
            _listTabGroup.value = newTabGroup
        }
        Log.d("TAG", _listTabGroup.value.toString())
    }


    override fun onCompleteTask(task: TaskUiState) {
        invertTaskFavorite(task)
    }

    override fun onTaskClicked(task: TaskUiState) {
        Log.d("TAG", "onTaskClicked")
    }

    override fun onFavorite(task: TaskUiState) {
        Log.d("TAG", "onFavorite")
    }

    override fun addNewTask(collectionId: Long, content: String) {
        viewModelScope.launch {
            taskRepo.addTask(content, collectionId)?.let { taskEntity ->
                val newTaskUiState = taskEntity.toTaskUiState()
                listTabGroup.value.let { listTabGroup ->
                    val newTabGroup = listTabGroup.map { tabGroup ->
                        val newPage = tabGroup.page.copy(
                            activeTaskList = tabGroup.page.activeTaskList + newTaskUiState
                        )
                        tabGroup.copy(page = newPage)
                    }
                    _listTabGroup.value = newTabGroup
                }
            }
        }
    }

    override fun addNewTaskToCurrentCollection(content: String) {
        viewModelScope.launch {
            listTabGroup.value.getOrNull(_currentSelectedCollectionIndex)?.let { currentTab ->
                val collectionId = currentTab.tab.id
                addNewTask(collectionId, content)
            }
        }
    }

    override fun updateCurrentCollectionIndex(index: Int) {
        _currentSelectedCollectionIndex = index
    }
}
