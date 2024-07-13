package com.example.quazz.core.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun QuazzSnackbar(
    paddingValues: PaddingValues,
    snackbarMessage: String,
    actionLabel: String? = null,
    onDismissed: () -> Unit = {},
    onActionPerformed: () -> Unit = {},
){
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = snackbarMessage) {
        if (snackbarMessage.isNotEmpty()) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                withDismissAction = true,
                actionLabel = actionLabel,
                message = snackbarMessage).let {
                when(it) {
                    SnackbarResult.Dismissed -> { onDismissed() }
                    SnackbarResult.ActionPerformed -> { onActionPerformed() }
                }
            }
        }
    }
    SnackbarHost(hostState = snackbarHostState, Modifier.padding(paddingValues))
}