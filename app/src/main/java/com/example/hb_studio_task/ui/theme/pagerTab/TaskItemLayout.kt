package com.example.hb_studio_task.ui.theme.pagerTab

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.dataStore.AppConstants
import com.example.hb_studio_task.ui.theme.component.common.RowComponent
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions
import com.example.hb_studio_task.utils.rememberRelativeTime


@Composable
fun LazyItemScope.TaskItemLayout(
    state: TaskUiState, action: TaskActions, groupId: Long
) {
    RowComponent(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                action.onTaskClicked(state.id)
            }
            .animateItem(
                tween(easing = LinearEasing),
                tween(easing = LinearEasing),
                tween(easing = LinearEasing)
            ),
        verticalAlignmentProp = Alignment.CenterVertically,
        horizontalArrangementProp = Arrangement.SpaceBetween,
    ) {

        Checkbox(
            checked = state.isCompleted, onCheckedChange = {
                action.onCompleteTask(
                    state.id,
                    state.isCompleted,
                    if (groupId == AppConstants.ID_FAVORITE_COLLECTION) {
                        state.collectionId
                    } else {
                        groupId
                    }
                )
            })
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = state.content,
                textDecoration = TextDecoration.LineThrough.takeIf { state.isCompleted })
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Completed: ${rememberRelativeTime(state.createdAt)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Icon(
            contentDescription = null,
            imageVector = Icons.Filled.Star,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    action.onFavorite(state.id, state.isFavorite)
                },
            tint = if (state.isFavorite) {
                Color(0xFFFFD700)
            } else {
                Color.Black
            },

            )
    }
}


