package com.example.quazz.app.presentation.quizzList.runQuizz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.R
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.model.Response
import com.example.quazz.app.presentation.quizzList.createQuizz.BottomAppBar
import com.example.quazz.core.components.outlinedTextField.QuazzOutlinedTextField
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme
import kotlinx.coroutines.launch

@Composable
fun RunQuizzScreen(
    popUp: () -> Unit,
    viewModel: RunQuizzViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    RunQuizzContent(
        popUp = popUp,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunQuizzContent(
    modifier: Modifier = Modifier,
    popUp: () -> Unit,
    state: RunQuizzState,
    onEvent: (RunQuizzEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState(pageCount = { state.quizz.questionnaires.size })
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        state.quizz.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popUp) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    CompleteDialog(
                        resultText = state.result
                    ) {
                        onEvent(RunQuizzEvent.OnComplete)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                isStartScreen = pagerState.currentPage == 0,
                onNext = { coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage.inc())
                } },
                onPrevious = { coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage.dec())
                }  }
            )
        }
    ){
        HorizontalPager(state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = modifier
                .padding(it)
                .fillMaxSize())
        { page ->
            Column(
                Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(QuazzTheme.dimension.paddingM)
            ) {
                when(val questionnaire = state.quizz.questionnaires[page]) {
                    is Questionnaire.ChoiceQuestion -> {
                        ElevatedCard(page, pagerState, questionnaire.question)
                        DisplayOptions(questionnaire = questionnaire, response = state.responses[page] as Response.BooleanList) { optionIndex ->
                            onEvent(RunQuizzEvent.UpdateBooleanList(optionIndex, page))
                        }
                    }
                    is Questionnaire.TextEntryQuestion -> {
                        ElevatedCard(page, pagerState, questionnaire.question)
                        QuazzOutlinedTextField(
                            value = (state.responses[page] as Response.StringItem).value,
                            onValueChange = { onEvent(RunQuizzEvent.UpdateStringItem(it, page)) },)
                    }
                }
            }
        }
    }
}

@Composable
private fun ElevatedCard(
    page: Int,
    pagerState: PagerState,
    question: String
) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = QuazzTheme.dimension.paddingM)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(
                start = QuazzTheme.dimension.paddingM,
                end = QuazzTheme.dimension.paddingM,
                top = QuazzTheme.dimension.paddingM
            ),
            text = page.inc().toString() + "/" + pagerState.pageCount,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            modifier = Modifier.padding(
                start = QuazzTheme.dimension.paddingM,
                end = QuazzTheme.dimension.paddingM,
                bottom = QuazzTheme.dimension.paddingM
            ),
            text = question
        )
    }
}

@Composable
private fun DisplayOptions(modifier: Modifier = Modifier, questionnaire: Questionnaire.ChoiceQuestion, response: Response.BooleanList, onClick: (Int) -> Unit) {
    questionnaire.options.forEachIndexed {
        index, option ->
        OutlinedButton(onClick = { onClick(index) },
            shape = OutlinedTextFieldDefaults.shape,
            border = if (response.value[index]) BorderStroke(2.0.dp, MaterialTheme.colorScheme.primary) else ButtonDefaults.outlinedButtonBorder(),
        ) {
            Text(option, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = QuazzTheme.dimension.paddingXS), maxLines = 1,)
        }
        Spacer(Modifier.padding(QuazzTheme.dimension.paddingXS))
    }
}

@Composable
fun CompleteDialog(
    modifier: Modifier = Modifier,
    resultText: List<Int>,
    onCompleteClick: () -> Unit) {
    var openAlertDialog by remember { mutableStateOf(false) }
    when {
        openAlertDialog -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog = false },
                confirmButton = {

                },
                dismissButton = {
                },
                title = { Text(stringResource(R.string.result)) },
                text = {
                    Column {
                        if (resultText.isEmpty()) {
                            Text(stringResource(R.string.every_answer_correct))
                        }
                        resultText.forEach {
                                index ->
                            Text(stringResource(R.string.question_number_wrong, index.inc()))
                        }
                    }
                }
            )
        }
    }
    TextButton(onClick = {
        onCompleteClick()
        openAlertDialog = true }) {
        Text(stringResource(R.string.complete))
    }
}

@Composable
@PreviewLightDark
private fun RunQuizzContentPreview() {
    val questionnaires = listOf(
        Questionnaire.ChoiceQuestion(
            "choice question",
            listOf("option1", "option2", "option3"),
            listOf(false, false, true)
        ),
        Questionnaire.TextEntryQuestion(
            "text entry question",
            "answer")
    )
    AppTheme {
        RunQuizzContent(
            state = RunQuizzState(
                quizz = Quizz(
                    id = "id",
                    title = "title",
                    description = "description",
                    questionnaires = questionnaires
                )
            ),
            onEvent = {},
            popUp = {}
        )
    }
}