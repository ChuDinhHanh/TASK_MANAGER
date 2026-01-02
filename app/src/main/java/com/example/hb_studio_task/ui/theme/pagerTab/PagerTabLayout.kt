package com.example.hb_studio_task.ui.theme.pagerTab

import TaskListPage
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.R
import com.example.hb_studio_task.ui.theme.component.SessionComponent
import com.example.hb_studio_task.ui.theme.component.home.EmptyNotification
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerTabLayout(listTabGroup: List<String>) {
    val scope = rememberCoroutineScope()
    // For the content below the tabs (if using Pager)
    var pageCount by remember { mutableIntStateOf(listTabGroup.size) }
    val pagerState = rememberPagerState { pageCount }


    /* Top Tab */
    PrimaryScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 0.dp, // Set to 0.dp to remove all default edge padding
    ) {
        repeat(pageCount + 1) { index ->
            Tab(text = {
                if (index < pageCount) {
                    Text(listTabGroup.getOrNull(index) ?: "New page")
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
    ) { pageIndex ->
        if (listTabGroup.getOrNull(pageIndex) != null) {
            SessionComponent(modifier = Modifier.padding(vertical = 16.dp)) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    TaskListPage(listTabGroup)
//                    EmptyNotification()
                }
            }
        } else {
            Text("HELLO2")
        }
    }
}