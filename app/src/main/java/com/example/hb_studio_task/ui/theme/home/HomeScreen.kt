package com.example.hb_studio_task.ui.theme.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hb_studio_task.MainViewModel
import com.example.hb_studio_task.ui.theme.component.common.SessionComponent
import com.example.hb_studio_task.ui.theme.floatAction.FloatActionButton
import com.example.hb_studio_task.ui.theme.pagerTab.PagerTabLayout
import com.example.hb_studio_task.ui.theme.pagerTab.state.TaskGroupUiState
import com.example.hb_studio_task.ui.theme.pagerTab.task.TaskActions
import com.example.hb_studio_task.ui.theme.topbar.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    listTaskGroup: List<TaskGroupUiState>,
    action: TaskActions,
    currentIndexPager: Int,
    mainViewModel: MainViewModel
) {
    val title by mainViewModel.appTitle.collectAsStateWithLifecycle()
    var isShowAddNoteBottomSheet by remember { mutableStateOf(false) }
    val url by mainViewModel.videoConfig.collectAsState() // Lấy URL video
    Scaffold(
        // QUAN TRỌNG: Phải có dòng này để thấy video phía dưới
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (listTaskGroup.isNotEmpty() && currentIndexPager != 0) {
                FloatActionButton(
                    modifier = Modifier
                        .background(
                            color = Color.Black.copy(0.2f), shape = RoundedCornerShape(12.dp)
                        )
                        .size(58.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    true,
                ) {
                    isShowAddNoteBottomSheet = true
                }
            }
        }) { innerPadding ->
        // innerPadding ở đây chính là độ cao của Status Bar
        // nó sẽ đẩy Column của bạn xuống dưới Status Bar để không bị đè chữ
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Topbar*/
            SessionComponent {
                TopBar(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f), title = title
                )
            }/*Show pager*/
            PagerTabLayout(listTaskGroup, action)/*Show bottom sheet*/
            if (isShowAddNoteBottomSheet) {
                var inputTaskContent by remember { mutableStateOf("") }
                var isLoading by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()
                ModalBottomSheet({
                    isShowAddNoteBottomSheet = false
                }) {
                    SessionComponent() {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Input Task Content")
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = inputTaskContent,
                                onValueChange = { value -> inputTaskContent = value },
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                shape = RoundedCornerShape(6.dp),
                                enabled = !isLoading && inputTaskContent.trim().isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = ButtonDefaults.buttonElevation(6.dp),
                                onClick = {
                                    isLoading = true
                                    try {
                                        action.addNewTaskToCurrentCollection(
                                            inputTaskContent
                                        )
                                        isShowAddNoteBottomSheet = false
                                    } catch (e: ArithmeticException) {

                                    } finally {
                                        isLoading = false
                                    }
                                }) {
                                Crossfade(
                                    targetState = isLoading, label = "loading_crossfade"
                                ) { targetIsLoading ->

                                    Row( // Wrap the indicator in a Row for alignment within the button
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (targetIsLoading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp), // Adjust size as needed
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                strokeWidth = 3.dp
                                            )
                                            Spacer(Modifier.width(16.dp))
                                        }
                                        Text(text = "Add New Task")
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

