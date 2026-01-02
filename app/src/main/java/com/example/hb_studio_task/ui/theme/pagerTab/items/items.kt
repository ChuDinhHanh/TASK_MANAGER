import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.ui.theme.pagerTab.TaskItemLayout
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskPageUiState


fun LazyListScope.showListTaskItems(key: String, state: List<String>) {
    itemsIndexed(state, key = { _, item -> "$key${item}" }, contentType = { _, item -> item::class.java.name }) { _, item ->
        TaskItemLayout(item)
    }
}

fun LazyListScope.spacer(height: Int) {
    item {
        Spacer(modifier = Modifier.height(height.dp))
    }
}

/* Hàm xử lý khi rỗng*/

fun LazyListScope.emptyState(key:String, state: TaskPageUiState){

}