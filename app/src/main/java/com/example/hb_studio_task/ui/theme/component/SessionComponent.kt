package com.example.hb_studio_task.ui.theme.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SessionComponent(
    modifier: Modifier = Modifier,
    paddingHorizontal: Dp = 16.dp,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = paddingHorizontal)
    ) {
        content()
    }
}