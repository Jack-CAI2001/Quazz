package com.example.quazz.app.presentation.quizzList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quazz.R
import com.example.quazz.app.model.Quizz
import com.example.quazz.core.components.WithAnimatedVisibilityScope
import com.example.quazz.core.components.WithSharedTransitionScope
import com.example.quazz.core.components.animations.expandFade
import com.example.quazz.core.components.animations.fadeOutAndShrink
import com.example.quazz.core.components.outlinedTextField.LoadingScreen
import com.example.quazz.navigation.Route
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme.dimension
import kotlinx.coroutines.launch

@Composable
fun QuizzListScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: QuizzListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    Content(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: QuizzListState,
    onEvent: (QuizzListEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabItems = listOf(R.string.created, R.string.quizz)
    val pagerState = rememberPagerState {
        tabItems.size
    }
    val selectedTabIndex by remember {
        derivedStateOf { pagerState.currentPage }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        scope.launch {
                            onEvent(QuizzListEvent.OnTabClick(index))
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = stringResource(id = item))
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when {
                state.isLoading -> LoadingScreen()
                else -> Content(state.quizzList, { quizz -> navController.navigate(Route.QuizzRoute(quizz, selectedTabIndex))}) {
                    navController.navigate(Route.CreateRoute) // result activity ?
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Content(
    quizzCreatedList: List<Quizz>,
    onQuizzCardClick: (String) -> Unit,
    onActionButton: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    var expandable by remember { mutableStateOf(false) }
    expandable = lazyListState.isScrollInProgress
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = !expandable,
                enter = expandFade(),
                exit = fadeOutAndShrink()) {
                WithSharedTransitionScope {
                    WithAnimatedVisibilityScope {
                        ExtendedFloatingActionButton(
                            modifier = Modifier.sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "FAB_EXPLODE_BOUNDS_KEY"),
                                animatedVisibilityScope = this,
                            ),
                            onClick = onActionButton,
                            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "") },
                            text = {
                                Text(stringResource(R.string.add_quizz))
                            },
//                        elevation = FloatingActionButtonDefaults.elevation(
//                            defaultElevation = 0.dp
//                        ) uncomment if elevation bug persist
                        )
                    }
                }
            }
        },
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimension.paddingM),
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item { Spacer(Modifier.size(dimension.paddingXS)) }
            items(quizzCreatedList) {
                    quizz ->
                QuizzCard(quizz) {
                    onQuizzCardClick(quizz.id)
                }
            }
            item { Spacer(Modifier.size(dimension.paddingXS)) }
        }
    }
}

@Composable
private fun QuizzCard(
    quizz: Quizz,
    onQuizzCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimension.paddingM),
        onClick = onQuizzCardClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimension.paddingM),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = quizz.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = quizz.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.icon_arrow_right)
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun CreateContentPreview() {
    val quizzList = listOf(
        Quizz(title = "title", description = "description"),
        Quizz(title = "title", description = "description"),
        Quizz(title = "title", description = "description"),
        Quizz(title = "title", description = "description"),
        Quizz(title = "title", description = "description"),
        Quizz(title = "title", description = "description"),
        Quizz(title = "title", description = "description"),
        Quizz(title = "title", description = "description")
    )
    AppTheme {
        Content(
            navController = rememberNavController(),
            state = QuizzListState(quizzList = quizzList),
            onEvent = {},
        )
    }
}