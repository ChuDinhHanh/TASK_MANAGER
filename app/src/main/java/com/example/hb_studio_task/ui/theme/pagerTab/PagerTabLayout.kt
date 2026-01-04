package com.example.hb_studio_task.ui.theme.pagerTab

import TaskListPage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.ui.theme.component.common.SessionComponent
import com.example.hb_studio_task.ui.theme.pagerTab.state.TabUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerTabLayout(actions: TaskActions) {
    val listTaskUiState = listOf<TabUiState>(
        TabUiState(
            id = 1,
            title = "TAB 1"
        ),
        TabUiState(
            id = 2,
            title = "TAB 2"
        )
    )

    val cloneDataActiveTaskUiState = listOf<TaskUiState>(
        TaskUiState(
            id = 1,
            content = "Học Jetpack Compose",
            isFavorite = true,
            isCompleted = false,
            collectionId = 1,
            updatedAt = 1704345600000L,
            stringUpdatedAt = "04/01/2026",
            createdAt = 1704345600000L
        ),
        TaskUiState(
            id = 2,
            content = "Mua đồ ăn sáng",
            isFavorite = false,
            isCompleted = true,
            collectionId = 1,
            updatedAt = 1704355600000L,
            stringUpdatedAt = "04/01/2026",
            createdAt = 1704345600000L
        )
    )

    val cloneDataCompletedTaskUiState = listOf<TaskUiState>(
        TaskUiState(
            id = 3,
            content = "Tập gym lúc 5h chiều",
            isFavorite = true,
            isCompleted = false,
            collectionId = 2,
            updatedAt = 1704365600000L,
            stringUpdatedAt = "04/01/2026",
            createdAt = 1704345600000L
        ),
        TaskUiState(
            id = 4,
            content = "Đọc sách 30 phút",
            isFavorite = false,
            isCompleted = false,
            collectionId = 2,
            updatedAt = 1704375600000L,
            stringUpdatedAt = "05/01/2026",
            createdAt = 1704345600000L
        )
    )

    val listTaskPage =
        listOf(
            TaskPageUiState(cloneDataActiveTaskUiState, cloneDataCompletedTaskUiState),
            TaskPageUiState(cloneDataActiveTaskUiState, cloneDataCompletedTaskUiState)
        )

    val scope = rememberCoroutineScope()
    // For the content below the tabs (if using Pager)
    var pageCount by remember { mutableIntStateOf(listTaskUiState.size) }
    val pagerState = rememberPagerState { pageCount }


    /* Top Tab */
    PrimaryScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 0.dp, // Set to 0.dp to remove all default edge padding
    ) {
        repeat(pageCount + 1) { index ->
            Tab(text = {
                if (index < pageCount) {
                    Text(listTaskUiState.getOrNull(index)?.title ?: "New page")
                } else {
                    Text("+ New Task")
                }
            }, selected = index == pagerState.currentPage, onClick = {
                if (index < pageCount) {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                } else {
                    pageCount += 1
                }
            })
        }/*Bonus tag*/
    }
    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 2
    ) { pageIndex ->
        if (listTaskUiState.getOrNull(pageIndex) != null) {
            SessionComponent(modifier = Modifier.padding(vertical = 16.dp)) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    TaskListPage(listTaskPage[pageIndex], actions = actions)
                }
            }
        } else {
            Text("HELLO2")
        }
    }
}