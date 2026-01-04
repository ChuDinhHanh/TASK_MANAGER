package com.example.hb_studio_task.ui.theme.pagerTab

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.ui.theme.component.common.RowComponent
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions

@Composable
fun LazyItemScope.TaskItemLayout(
    state: TaskUiState,
    action: TaskActions
) {
    RowComponent(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                action.onTaskClicked(state)
            }
            .animateItem(
                tween(easing = LinearEasing),
                tween(easing = LinearEasing),
                tween(easing = LinearEasing)
            ),
        verticalAlignmentProp = Alignment.CenterVertically,
        horizontalArrangementProp = Arrangement.Start,
    ) {

        Checkbox(
            checked = true,
            onCheckedChange = {
                action.onCompleteTask(state)
            }
        )
        Column(
            modifier = Modifier
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = state.content,
                modifier = Modifier.padding(horizontal = 4.dp),
                textDecoration = TextDecoration.LineThrough.takeIf { state.isCompleted }
            )

        }
        Text(if (true) "👍" else "👎", modifier = Modifier.clickable {
            action.onFavorite(state)
        })
    }
}


