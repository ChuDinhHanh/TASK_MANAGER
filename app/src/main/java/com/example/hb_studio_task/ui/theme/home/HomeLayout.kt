package com.example.hb_studio_task.ui.theme.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hb_studio_task.MainViewModel
import com.example.hb_studio_task.ui.theme.component.SessionComponent
import com.example.hb_studio_task.ui.theme.floatAction.FloatActionButton
import com.example.hb_studio_task.ui.theme.pagerTab.PagerTabLayout
import com.example.hb_studio_task.ui.theme.topbar.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(mainViewModel: MainViewModel = hiltViewModel()) {
    val listTabGroup = listOf<String>("HANH", "HUY")
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatActionButton(
            modifier = Modifier
                .background(
                    color = Color.Black.copy(0.2f), shape = RoundedCornerShape(12.dp)
                )
                .size(58.dp)
                .clip(RoundedCornerShape(12.dp)),
            true,
        ) {
            Log.d("Tag", "Click floating button")
        }

        /*innerPadding = nội dung UI của bạn không bị che khuất bởi các yếu tố hệ thống như thanh trạng thái*/
    }) { innerPadding ->
        if (listTabGroup.isNotEmpty()) {
            AnimatedVisibility(listTabGroup.size > 0) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /*Topbar*/
                    SessionComponent {
                        TopBar(
                            Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                        )
                    }
                    /*Show pager*/
                    PagerTabLayout(listTabGroup)
                }
            }
        } else {
            Text("EMPTY")
        }
    }
}
