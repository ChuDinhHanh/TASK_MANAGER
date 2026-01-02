package com.example.hb_studio_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hb_studio_task.repository.TaskRepo
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskGroupUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.toTabUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.toTaskUiState
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
) : ViewModel(), TaskDelegate {
    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> =
        MutableStateFlow(emptyList());
    val listTaskGroup = _listTabGroup.asStateFlow();

    init {
        viewModelScope.launch {
            val listTaskCollections = taskRepo.getTaskCollection()
            if (listTaskCollections.isEmpty()) {
                taskRepo.addTaskCollection("My Task 1!")?.let { collection ->
                    val id = collection.id
                    taskRepo.addTask("Task3",id)
                }
            }
            val listTabGroupUiState = taskRepo.getTaskCollection().map { taskCollections ->
                val collectionId = taskCollections.id
                val listTaskUiState =
                    taskRepo.getTaskByCollectionId(collectionId).map { taskEntity ->
                        taskEntity.toTaskUiState()
                    }
                val tabUiState = taskCollections.toTabUiState()
                TaskGroupUiState(
                    tabUiState, TaskPageUiState(
                        activeTaskList = listTaskUiState.filter { !it.isCompleted },
                        completedTaskList = listTaskUiState.filter { it.isCompleted },
                    )
                )
            }
            _listTabGroup.value = listTabGroupUiState
        }
    }

    override fun invertTaskFavorite(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}


interface TaskDelegate {
    fun invertTaskFavorite(taskUiState: TaskUiState) = Unit
    fun invertTaskCompleted(taskUiState: TaskUiState) = Unit
    fun addNewTask(collectionId: Long, content: String) = Unit
    fun addNewTaskToCurrentCollection(content: String) = Unit
    fun updateCurrentCollectionId(collectionId: Long) = Unit
    fun currentCollectionId(): Long = -1L
    fun addNewCollection(title: String) = Unit
    fun requestAddNewCollection(): Unit = Unit
    fun requestUpdateCollection(collectionId: Long) = Unit
    fun requestSortTasks(collectionId: Long) = Unit
}

sealed class MainEvent {
//    data object RequestAddNewCollection : MainEvent()
//    data class RequestShowBottomSheetOptions(val list: List<AppMenuItem>) : MainEvent()
}