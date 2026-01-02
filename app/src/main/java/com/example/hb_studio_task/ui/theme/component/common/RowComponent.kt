package com.example.hb_studio_task.ui.theme.component.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RowComponent(
    modifier: Modifier,
    verticalAlignmentProp: Alignment.Vertical,
    horizontalArrangementProp: Arrangement.Horizontal,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignmentProp,
        horizontalArrangement = horizontalArrangementProp
    ) {
        content()
    }
}