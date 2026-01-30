package com.example.hb_studio_task.ui.theme

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hb_studio_task.MainEvent
import com.example.hb_studio_task.MainViewModel
import com.example.hb_studio_task.R
import com.example.hb_studio_task.ui.theme.component.common.SessionComponent
import com.example.hb_studio_task.ui.theme.home.HomeScreen
import com.example.hb_studio_task.utils.FireworkTrigger
import com.example.hb_studio_task.utils.ShowSnakeBar
import com.example.hb_studio_task.utils.vibrateSuccess
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksApp(mainViewModel: MainViewModel) {/* Variable */
    val taskDelegate = remember { mainViewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val listTaskGroup by taskDelegate.listTabGroup.collectAsStateWithLifecycle()/* Firework */
    val fireworkComp by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.firework_lottie))
    val fireworks by taskDelegate.firework.collectAsState()
    var isShowAddCollectionBottomSheet by remember { mutableStateOf(false) }
    val currentIndexPager = taskDelegate.currentSelectedCollectionIndex.collectAsState()
    val context = LocalContext.current
    var listActionTask by remember { mutableStateOf<List<AppMenuItem>?>(null) }

    /* Function logic */
    LaunchedEffect(Unit) {
        taskDelegate.eventFlow.collect {
            when (it) {
                MainEvent.RequestAddNewCollection -> {
                    isShowAddCollectionBottomSheet = true
                }/*this@MainActivity: có nghĩa là tôi muốn dùng đối
                        tượng của lớp main chứ không phải lớp gần nhất (this)
                         */
                MainEvent.RequestVibrate -> {
                    vibrateSuccess(context)
                }

                MainEvent.AllTaskCompleted -> {
                    taskDelegate.triggerFirework()
                }

                is MainEvent.ShowSnakeBar -> {
                    ShowSnakeBar(it.notification, snackbarHostState)
                }

                is MainEvent.RequestShowButtonSheetOption -> {
                    listActionTask = it.list
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(snackbarHostState)
        }) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreen(listTaskGroup, taskDelegate, currentIndexPager.value, mainViewModel)
        }
    }

    /* Firework*/
    fireworks.forEach { firework ->
        key(firework.id) {
            FireworkTrigger(fireworkComp) {
                taskDelegate.removeFirework(
                    firework.id
                )
            }
        }
    }


    /* Modal Option */
    if (!listActionTask.isNullOrEmpty()) {
        ModalBottomSheet({
            listActionTask = null
        }) {
            SessionComponent() {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Choose option action")
                    Spacer(modifier = Modifier.height(40.dp))
                    listActionTask?.forEach {
                        ElevatedButton(onClick = {
                            val result = it.action.invoke()
                            listActionTask = null
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(it.title)
                        }
                    }
                }
            }
        }
    }

    /* Modal */
    if (isShowAddCollectionBottomSheet) {
        var inputTaskContent by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        ModalBottomSheet({
            isShowAddCollectionBottomSheet = false
        }) {
            SessionComponent() {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Input Task Collection")
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
                                taskDelegate.addNewCollection(inputTaskContent)
                                isShowAddCollectionBottomSheet = false
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