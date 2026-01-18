import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.R
import com.example.hb_studio_task.ui.theme.component.common.RowComponent
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions

@Composable
fun TaskListPage(state: TaskPageUiState, actions: TaskActions, groupId: Long, title: String) {
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
                topCorner(title = title)
                showListTaskItems("State", state.activeTaskList, actions, groupId)
                bottomCorner()
            }/* Show task completed */
            if (state.completedTaskList.isNotEmpty()) {
                spacerLazyList(16)
                topCorner(desciption = "Completed ${state.completedTaskList.size} task${if (state.completedTaskList.size >= 2) "s" else ""}")
                showListTaskItems("State", state.completedTaskList, actions, groupId)
                bottomCorner()
            }
        }
    }
}


