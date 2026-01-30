package com.example.hb_studio_task

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hb_studio_task.dataStore.AppConstants
import com.example.hb_studio_task.database.entity.SortType
import com.example.hb_studio_task.repository.ConfigRepository
import com.example.hb_studio_task.repository.NotificationRepository
import com.example.hb_studio_task.repository.TaskRepo
import com.example.hb_studio_task.repository.VideoConfig
import com.example.hb_studio_task.ui.theme.AppMenuItem
import com.example.hb_studio_task.ui.theme.component.home.FireworkInstance
import com.example.hb_studio_task.ui.theme.pagerTab.state.TabUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskGroupUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.toTabUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.toTaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import updateTask
import javax.inject.Inject


// Step 1: config hilt + dagger
@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo,
    private val configRepo: ConfigRepository,
    private val notification: NotificationRepository
) : ViewModel(), TaskActions {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    /* Notification */
    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    /* Firework */
    private val _firework = MutableStateFlow<List<FireworkInstance>>(emptyList())
    val firework = _firework.asStateFlow()


    /* Share flow cho mấy nơi khác xài kiểu giống redux */
    private val _eventFlow: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    /* MutableStateFlow: HotFlow */
    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> =
        MutableStateFlow(emptyList());

    /* Remote config */
    private val _appTitle = MutableStateFlow("Loading...") // Giá trị ban đầu
    val appTitle = _appTitle.asStateFlow()
    private val _videoConfig = MutableStateFlow(VideoConfig())
    val videoConfig = _videoConfig.asStateFlow()

    /* asStateFlow = 🔒 Ẩn khả năng ghi – chỉ cho đọc
    1 Nguyên tắc đóng gói bên trong model có thể can thiệp nhưng bên ngoài chỉ được đocj thôi
    >> Read only bên ngoài chỉ được observe thôi oke chưa
    2 Kotlin mặc định val không ghi modifier = public
    */

    private var _currentSelectedCollectionIndex = MutableStateFlow(0);
    val currentSelectedCollectionIndex = _currentSelectedCollectionIndex.asStateFlow()

    fun fetchAndSaveToken() {
        notification.getFCMToken { newToken ->
            _token.value = newToken
            Log.d("FCM_TEST", "Token -${_token.value}")
        }
    }

    /*Fav*/
    val listTabGroup: StateFlow<List<TaskGroupUiState>> = _listTabGroup.map { groups ->
        val favTasks = groups.flatMap { group ->
            group.page.activeTaskList.filter { it.isFavorite } + group.page.completedTaskList.filter { it.isFavorite }
        }

        val favGroup = TaskGroupUiState(
            tab = TabUiState(AppConstants.ID_FAVORITE_COLLECTION, "FAV", true),
            page = TaskPageUiState(
                activeTaskList = favTasks.filter { !it.isCompleted }
                    .sortedByDescending { it.updatedAt }, completedTaskList = emptyList()
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
            launch {
                /*Remote config*/
                configRepo.getAppTitle { newTitle ->
                    _appTitle.value = newTitle
                }/*Get video*/
                configRepo.getVideoConfig { videoConfig ->
                    _videoConfig.value = videoConfig
                }
                combine(_videoConfig, _appTitle) { config, title ->
                    config.url.isNotEmpty() && title.isNotEmpty()
                }.collect { ready ->
                    if (ready) {
                        _isReady.value = true
                    }
                }
            }
            launch {
                /*Data*/
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

            launch {
                delay(10000)
                if (!_isReady.value) {
                    _isReady.value = true
                }
            }

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

    override fun requestUpdateCollection(collectionId: Long) {
        viewModelScope.launch {
            val list = listOf(AppMenuItem("Delete Collection") {
                viewModelScope.launch {
                    val result = deleteCollectionById(collectionId)
                    _eventFlow.emit(MainEvent.ShowSnakeBar("Xóa thành công"))
                }
            }, AppMenuItem("Rename Collection") {
                Log.d("TAG", "Request Rename Collection $collectionId")
            })
            _eventFlow.emit(MainEvent.RequestShowButtonSheetOption(list))
        }
    }

    private suspend fun deleteCollectionById(collectionId: Long): Boolean {
        val result = taskRepo.deleteCollectionById(collectionId)
        if (result) {
            _listTabGroup.value.let { item1 ->
                val newData = item1.filter { item2 -> item2.tab.id != collectionId }
                _listTabGroup.value = newData
            }
        }
        return result
    }


    private fun sortCollectionBy(collectionId: Long, sortType: SortType) {
        _listTabGroup.update { currentData ->
            currentData.map { group ->
                if (group.tab.id == collectionId) {
                    val newData = when (sortType) {
                        SortType.CREATED_DATE -> group.page.activeTaskList.sortedByDescending { it.createdAt }
                        SortType.FAVORITE -> group.page.activeTaskList.sortedByDescending { it.isFavorite }
                    }
                    group.copy(page = group.page.copy(activeTaskList = newData))
                } else {
                    group
                }
            }
        }
    }


    override fun requestSortTasks(collectionId: Long) {
        viewModelScope.launch {
            _eventFlow.emit(
                MainEvent.RequestShowButtonSheetOption(
                    listOf(
                        AppMenuItem("Sort by favourite") {
                            sortCollectionBy(collectionId, SortType.FAVORITE)
                        },

                        AppMenuItem("Sort by timeStamp") {
                            sortCollectionBy(collectionId, SortType.CREATED_DATE)
                        },
                    )
                )
            )
        }
    }

}


sealed class MainEvent {
    data object RequestAddNewCollection : MainEvent()
    data object RequestVibrate : MainEvent()
    data object AllTaskCompleted : MainEvent()
    data class RequestShowButtonSheetOption(val list: List<AppMenuItem>) : MainEvent()
    data class ShowSnakeBar(val notification: String) : MainEvent()
}

