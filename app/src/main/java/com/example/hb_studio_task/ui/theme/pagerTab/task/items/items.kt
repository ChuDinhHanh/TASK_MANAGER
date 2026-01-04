import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hb_studio_task.R
import com.example.hb_studio_task.ui.theme.pagerTab.TaskItemLayout
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions


fun LazyListScope.showListTaskItems(key: String, state: List<TaskUiState>, actions: TaskActions) {
    itemsIndexed(
        state,
        key = { _, item -> "$key${item}" },
        contentType = { _, item -> item::class.java.name }) { _, item ->
        TaskItemLayout(item,actions)
    }
}

fun LazyListScope.spacer(height: Int) {
    item {
        Spacer(modifier = Modifier.height(height.dp))
    }
}

/* Hàm xử lý khi rỗng*/
fun LazyListScope.emptyState(key: String? = null, state: List<String>? = null) {
    if (state.isNullOrEmpty()) {
        item(key) {
            Log.d("TAG", "SHOW EMPTY UI")
            /*1. Khởi tạo trạng thái*/
            val state = remember {
                /*false: Lúc đầu ẩn*//*apply:true là cho phép xuất hiện*/
                MutableTransitionState(false).apply {
                    targetState = true
                }
            }
            AnimatedVisibility(
                visibleState = state, enter = slideInVertically(
                    initialOffsetY = { it / 4 }) + fadeIn(
                    animationSpec = tween(durationMillis = 1000)
                ), exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }) + fadeOut()
            ) {
                Column(modifier = Modifier.fillParentMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxWidth()
                    ) {

                        Image(
                            painter = painterResource(R.drawable.empty_task),
                            contentDescription = "Empty images",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                "All task completed", style = MaterialTheme.typography.titleLarge
                            )
                            Text("Nice work!", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}