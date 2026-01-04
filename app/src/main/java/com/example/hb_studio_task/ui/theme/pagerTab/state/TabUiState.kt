package com.example.hb_studio_task.ui.theme.pagerTab.state

import com.example.hb_studio_task.database.entity.TaskCollections

data class TabUiState(
    val id: Long,
    val title: String,
)

fun TaskCollections.toTabUiState(): TabUiState {
    return TabUiState(
        id = this.id,
        title = this.title
    )
}

