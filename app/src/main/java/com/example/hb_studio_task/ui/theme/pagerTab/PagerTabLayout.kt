package com.example.hb_studio_task.ui.theme.pagerTab

import TaskListPage
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.ui.theme.component.common.SessionComponent
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskGroupUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions
import kotlinx.coroutines.launch

/* tại sao ở mainActivity là model mà bên này là interface đơn giản đa hình
* 1 - 1 class có thể được dùng như 1 interface mà nó implement
* 2 - ở UI không được có model vì nó vi phạm clean code */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerTabLayout(
    state: List<TaskGroupUiState>,
    taskDelegate: TaskActions
) {
    /* Logic*/

    /* Scope for thí composable manager lifecycle */
    val scope = rememberCoroutineScope()/* For animation render */
    // For the content below the tabs (if using Pager)
    var pageCount by remember { mutableIntStateOf(state.size) }
    val pagerState = rememberPagerState { pageCount }/* Top Tab */


    LaunchedEffect(Unit) {
        /* snapshotFlow: sẽ theo dõi giá trị của pagerState.currentPage mỗi khi
        *  thay đổi nó sẽ emit mới vào dòng chảy flow
        * */

        /* collect: để hứng giá trị được emit ra */
        snapshotFlow { pagerState.currentPage }.collect { index ->
            taskDelegate.updateCurrentCollectionIndex(index)
            Log.d("TAG", index.toString())
        }
    }


    /* UI */
    PrimaryScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 0.dp, // Set to 0.dp to remove all default edge padding
    ) {
        repeat(pageCount + 1) { index ->
            Tab(text = {
                if (index < pageCount) {
                    Text(state.getOrNull(index)?.tab?.title ?: "New page")
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
        state = pagerState, beyondViewportPageCount = 2
    ) { pageIndex ->
        if (state.getOrNull(pageIndex) != null) {
            SessionComponent(modifier = Modifier.padding(vertical = 16.dp)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TaskListPage(state[pageIndex].page, taskDelegate)
                }
            }
        } else {
            Text("HELLO2")
        }
    }
}