package com.example.quazz.app.presentation.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quazz.navigation.Route
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme

@Composable
fun CreateScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: CreateListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    CreateContent(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun CreateContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: CreateListState,
    onEvent: (CreateListEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    var expandable by remember { mutableStateOf(false) }
    expandable = lazyListState.isScrollInProgress
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        floatingActionButton = {
            OutlinedButton(onClick = {
                navController.navigate(Route.CreateRoute.route) // result activity ?
            }) {
                this.AnimatedVisibility(visible = !expandable) {
                    Text(text = "Add Quizz")
                }
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        },
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(QuazzTheme.dimension.paddingM),
            state = lazyListState,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(top = QuazzTheme.dimension.paddingM)
        ) {
            items(15) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Create",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun CreateContentPreview() {
    AppTheme {
        CreateContent(
            state = CreateListState(),
            navController = rememberNavController(),
            onEvent = {}
        )
    }
}