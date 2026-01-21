import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.hb_studio_task.ui.theme.pagerTab.TaskItemLayout
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions

/* item - cần index *//* items Không cần index *//* itemsIndexed: cần key lẫn - cần custom key theo index + data*/

fun LazyListScope.showListTaskItems(
    key: String, state: List<TaskUiState>, actions: TaskActions, groupId: Long
) {
    itemsIndexed(
        state,
        key = { _, item -> "$key${item}" },
        contentType = { _, item -> item::class.java.name }) { _, item ->
        TaskItemLayout(item, actions, groupId)
    }
}

fun LazyListScope.spacerLazyList(height: Int) {
    item {
        Spacer(modifier = Modifier.height(height.dp))
    }
}

fun LazyListScope.horizontalDivider() {
    item {
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    }
}


/* Hàm xử lý khi rỗng*/
fun LazyListScope.emptyState(key: String? = null, img: Int, state: List<String>? = null) {
    if (state.isNullOrEmpty()) {
        item(key) {
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.8f)
                                .fillMaxWidth(),
                        ) {
                            AsyncImage(
                                model = img,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(0.2f)
                                .fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    "All task completed",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text("Nice work!", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}


fun LazyListScope.spacer(height: Int) {
    item {
        Spacer(modifier = Modifier.height(height.dp))
    }
}


fun LazyListScope.topCorner(
    title: String? = null,
    description: String? = null,
    isOpen: Boolean = false,
    taskActions: TaskActions,
    collectionId: Long,
    onClick: () -> Unit
) {
    item() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)
                )
                .clickable() {
                    onClick()
                }) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (title != null) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth(),
                        onClick = {
                            taskActions.requestUpdateCollection(collectionId)
                        }) {
                        Text("S", style = TextStyle(color = Color.Black))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    ElevatedButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth(),
                        onClick = {
                            taskActions.requestUpdateCollection(collectionId)
                        }) {
                        Text("D", style = TextStyle(color = Color.Black))
                    }
                }
                if (description != null) {
                    Text(description, modifier = Modifier.weight(1f))
                    Text(if (isOpen) "Close" else "Expand")
                }
            }

        }
    }
}


fun LazyListScope.bottomCorner() {
    item() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    color = Color.White, shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp)
                )
        )
    }
}

fun List<TaskUiState>.updateTask(
    targetId: Long, transform: (TaskUiState) -> TaskUiState
): List<TaskUiState> {
    return this.map {
        if (it.id == targetId) {
            transform(it)
        } else {
            it
        }
    }
}