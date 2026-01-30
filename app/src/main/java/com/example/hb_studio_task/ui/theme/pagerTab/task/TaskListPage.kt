import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.R
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions

@Composable
fun TaskListPage(
    state: TaskPageUiState,
    actions: TaskActions,
    groupId: Long,
    title: String,
    isFav: Boolean = false
) {
    var isOpen by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.activeTaskList.isEmpty() && state.completedTaskList.isEmpty()) {
            emptyState(img = R.drawable.empty_task)
        } else {
            if (state.activeTaskList.isEmpty()) {
                emptyState(img = R.drawable.complete_all_task)
            } else {/* Show task*/
                if (!isFav) {
                    topCorner(
                        title = title, taskActions = actions, collectionId = groupId, onClick = {})
                }
                showListTaskItems("State", state.activeTaskList, actions, groupId)
            }/* Show task completed */
            if (state.completedTaskList.isNotEmpty()) {
                spacerLazyList(16)
                topCorner(
                    description = "Completed (${state.completedTaskList.size}) task${if (state.completedTaskList.size >= 2) "s" else ""}",
                    isOpen = isOpen,
                    taskActions = actions,
                    collectionId = groupId,
                    onClick = {
                        isOpen = !isOpen
                    })
                if (isOpen) {
                    showListTaskItems("State", state.completedTaskList, actions, groupId)
                }
            }
        }
    }
}


