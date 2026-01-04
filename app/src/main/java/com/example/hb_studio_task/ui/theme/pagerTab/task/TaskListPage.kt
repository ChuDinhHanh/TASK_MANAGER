import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions

@Composable
fun TaskListPage(state: TaskPageUiState, actions: TaskActions) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        showListTaskItems("State", state.activeTaskList, actions)
        showListTaskItems("State", state.completedTaskList, actions)
    }
}
