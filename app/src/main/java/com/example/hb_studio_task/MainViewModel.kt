package com.example.hb_studio_task

import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hb_studio_task.repository.TaskRepo
import com.example.hb_studio_task.ui.theme.component.home.FireworkInstance
import com.example.hb_studio_task.ui.theme.pagerTab.state.TabUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskGroupUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.toTabUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.toTaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import updateTask
import javax.inject.Inject

const val ID_FAVORITE_LIST = -1000L


// Step 1: config hilt + dagger
@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
) : ViewModel(), TaskActions {
    /* Firework */
    private val _firework = MutableStateFlow<List<FireworkInstance>>(emptyList())
    val firework = _firework.asStateFlow()

    /* Share flow cho mấy nơi khác xài kiểu giống redux */
    private val _eventFlow: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    /* MutableStateFlow: HotFlow */
    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> =
        MutableStateFlow(emptyList());

    /* asStateFlow = 🔒 Ẩn khả năng ghi – chỉ cho đọc
    1 Nguyên tắc đóng gói bên trong model có thể can thiệp nhưng bên ngoài chỉ được đocj thôi
    >> Read only bên ngoài chỉ được observe thôi oke chưa
    2 Kotlin mặc định val không ghi modifier = public
    */

    private var _currentSelectedCollectionIndex = MutableStateFlow(0);
    val currentSelectedCollectionIndex = _currentSelectedCollectionIndex.asStateFlow()

    /*Fav*/


    val listTabGroup: StateFlow<List<TaskGroupUiState>> = _listTabGroup.map { groups ->
        val favTasks = groups.flatMap { group ->
            group.page.activeTaskList.filter { it.isFavorite } + group.page.completedTaskList.filter { it.isFavorite }
        }

        val favGroup = TaskGroupUiState(
            tab = TabUiState(ID_FAVORITE_LIST, "FAV", true), page = TaskPageUiState(
                activeTaskList = favTasks.filter { !it.isCompleted }
                    .sortedByDescending { it.updatedAt },
                completedTaskList = emptyList()
            )
        )
        listOf(favGroup) + groups
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )


    init {
        viewModelScope.launch {
            val listTasksCollections = taskRepo.getTaskCollection()/* Add favorite task */
            val listTabGroupUiState = listTasksCollections.let { value ->
                value.map { taskCollection ->
                    val collectionId = taskCollection.id
                    val listTaskUiState =
                        taskRepo.getTaskByCollectionId(collectionId).map { taskEntity ->
                            taskEntity.toTaskUiState()
                        }
                    val tabUiState = taskCollection.toTabUiState()
                    TaskGroupUiState(
                        tabUiState, TaskPageUiState(
                            activeTaskList = listTaskUiState.filter { !it.isCompleted },
                            completedTaskList = listTaskUiState.filter { it.isCompleted })
                    )
                }
            }


            _listTabGroup.value = listTabGroupUiState
        }
    }


    fun triggerFirework() {
        val newFirework = FireworkInstance()
        _firework.update { it + newFirework }
    }

    fun removeFirework(id: Long) {
        _firework.update { list -> list.filter { it.id != id } }
    }

    override fun onCompleteTask(taskId: Long, isComp: Boolean, groupId: Long): Job =
        viewModelScope.launch(Dispatchers.IO) {
            val isComp = !isComp
            if (!taskRepo.updateTaskCompleted(taskId, isComp)) {
                return@launch
            }
            _listTabGroup.update { currentGroup ->
                currentGroup.map { group ->

                    if (group.tab.id == groupId) {
                        val allTask = group.page.activeTaskList + group.page.completedTaskList;
                        val updatedTasks = allTask.updateTask(taskId) {
                            it.copy(isCompleted = isComp)
                        }
                        val newActiveTaskList = updatedTasks.filter { item -> !item.isCompleted }
                        val newCompletedTaskList = updatedTasks.filter { item -> item.isCompleted }
                        val hadFinishYet = newActiveTaskList.isEmpty() && !group.tab.hadFinished
                        if (hadFinishYet) {
                            _eventFlow.emit(MainEvent.AllTaskCompleted)
                            taskRepo.updateCollectionCompleted(groupId, true)
                        }
                        group.copy(
                            tab = if (hadFinishYet) {
                                group.tab.copy(hadFinished = true)
                            } else {
                                group.tab
                            }, page = group.page.copy(
                                activeTaskList = newActiveTaskList.sortedByDescending { it.createdAt },
                                completedTaskList = newCompletedTaskList
                            )
                        )
                    } else {
                        group
                    }
                }
            }
            _eventFlow.emit(MainEvent.RequestVibrate)
        }

    override fun onTaskClicked(taskId: Long) {
        Log.d("TAG", "onTaskClicked")
    }

    override fun onFavorite(taskId: Long, isFav: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val isFav = !isFav
        if (!taskRepo.updateTaskFavorite(taskId, isFav)) {
            return@launch
        }
        _listTabGroup.update { currentGroup ->
            currentGroup.map { group ->
                group.copy(
                    page = group.page.copy(
                        activeTaskList = group.page.activeTaskList.updateTask(taskId) {
                            it.copy(isFavorite = isFav)
                        },
                        completedTaskList = group.page.completedTaskList.updateTask(taskId) {
                            it.copy(isFavorite = isFav)
                        },
                    )
                )
            }
        }
    }


    override fun addNewTask(collectionId: Long, content: String) {
        viewModelScope.launch {
            taskRepo.addTask(content, collectionId)?.let { taskEntity ->
                val newTaskUiState = taskEntity.toTaskUiState()
                _listTabGroup.update { list ->
                    list.map { group ->
                        if (group.tab.id == collectionId) {
                            val updatedList =
                                (group.page.activeTaskList + newTaskUiState).sortedByDescending { it.createdAt }
                            group.copy(page = group.page.copy(activeTaskList = updatedList))
                        } else {
                            group
                        }
                    }
                }
            }
        }
    }

    override fun addNewTaskToCurrentCollection(content: String) {
        viewModelScope.launch {
            listTabGroup.value.getOrNull(_currentSelectedCollectionIndex.value)?.let { currentTab ->
                val collectionId = currentTab.tab.id
                addNewTask(collectionId, content)
            }
        }
    }

    override fun updateCurrentCollectionIndex(index: Int) {
        _currentSelectedCollectionIndex.value = index
    }

    override fun addNewCollection(title: String) {
        viewModelScope.launch {
            taskRepo.addTaskCollection(title)?.let { taskCollections ->
                val tabUiState = taskCollections.toTabUiState()
                val newTabGroup =
                    TaskGroupUiState(tabUiState, TaskPageUiState(emptyList(), emptyList()))
                _listTabGroup.value += newTabGroup
            }
        }
    }

    override fun requestAddNewCollection() {
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestAddNewCollection)
        }
    }
}


sealed class MainEvent {
    data object RequestAddNewCollection : MainEvent()
    data object RequestVibrate : MainEvent()
    data object AllTaskCompleted : MainEvent()
}

