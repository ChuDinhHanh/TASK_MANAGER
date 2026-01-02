package com.example.hb_studio_task.ui.theme.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TopBar(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Text("HELLO KOTLIN", modifier = Modifier.align(Alignment.Center))
        Box(
            modifier = Modifier
                .size(45.dp)
                .align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ) {
            Text("123")
        }
    }
}