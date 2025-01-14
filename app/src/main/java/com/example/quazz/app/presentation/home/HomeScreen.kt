package com.example.quazz.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.presentation.quizzList.quizz.QuizzContent
import com.example.quazz.app.presentation.quizzList.quizz.QuizzState
import com.example.quazz.core.components.outlinedTextField.LoadingScreen
import com.example.quazz.ui.theme.QuazzTheme

@Composable
fun HomeScreen(paddingValues: PaddingValues, runQuizz: ((Quizz) -> Unit), viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val pagerState = PagerState(pageCount = {state.quizzList.size})

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) { page ->
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect {
                viewModel.updateCurrentIndex(it)
                viewModel.onEvent(HomeEvent.LastQuizz)
            }
        }
        if (state.isLoading) {
            LoadingScreen()
        } else {
            Scaffold {
                QuizzContent(
                    modifier = Modifier.padding(it),
                    state = QuizzState(
                        isLoading = false,
                        isEdit = false,
                        quizz = state.quizzList[page]
                    )
                )
                Column(
                    modifier = Modifier.padding(it).fillMaxSize()
                        .padding(
                            bottom = QuazzTheme.dimension.paddingXL,
                            end = QuazzTheme.dimension.paddingM,
                            start = QuazzTheme.dimension.paddingM
                        ),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End) {
                    OutlinedIconButton(onClick = { viewModel.onEvent(HomeEvent.AddQuizz) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                    OutlinedIconButton(onClick = {runQuizz(state.quizzList[page])}) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
                    }
                }
            }
        }
    }
}