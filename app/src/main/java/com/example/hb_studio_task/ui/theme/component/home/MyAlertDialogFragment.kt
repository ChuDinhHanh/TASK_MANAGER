package com.example.hb_studio_task.ui.theme.component.home

import android.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MyAlertDialog() {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(onDismissRequest = {
            // Dismiss the dialog when the user clicks outside or presses the back button
            showDialog = false
        }, title = {
            Text(text = "Dialog Title")
        }, text = {
            Text(text = "This is a simple alert dialog example in Compose.")
        }, confirmButton = {
            TextButton(
                onClick = {
                    showDialog = false
                    // Perform action here
                }) {
                Text("Confirm")
            }
        }, dismissButton = {
            TextButton(
                onClick = {
                    showDialog = false
                }) {
                Text("Dismiss")
            }
        })
    }
}