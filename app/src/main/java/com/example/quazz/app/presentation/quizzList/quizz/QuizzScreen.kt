package com.example.quazz.app.presentation.quizzList.quizz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.R
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.Quizz
import com.example.quazz.core.components.QuazzTopAppBar
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizzScreen(
    popUp: () -> Unit,
    viewModel: QuizzViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (state.isEdit) {
        QuizzEditScreen(
            state = state,
            onEvent = viewModel::onEvent,
        )
    } else {
        Scaffold(
            topBar = {
                QuazzTopAppBar(
                    title = stringResource(R.string.quizz),
                    canNavigateBack = true,
                    navigateUp = popUp,
                    isLoading = state.isLoading
                ) {
                    IconButton(onClick = {showBottomSheet = true}) {
                        Icon(Icons.Default.MoreVert, "", tint = MaterialTheme.colorScheme.background)
                    }
                }
            }
        ) {
            QuizzContent(
                modifier = Modifier.padding(it),
                state = state,
            )
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Column(
                        Modifier
                            .padding(
                                bottom = QuazzTheme.dimension.paddingXL,
                                end = QuazzTheme.dimension.paddingM,
                                start = QuazzTheme.dimension.paddingM
                            )
                            .clip(RoundedCornerShape(QuazzTheme.dimension.paddingS))
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        TextIconStartPosition(
                            icon = {
                                Icon(
                                    modifier = Modifier.padding(QuazzTheme.dimension.paddingS),
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = ""
                                )
                            },
                            text = stringResource(R.string.run_quizz)
                        ) {
                            TODO()
                        }
                        TextIconStartPosition(
                            icon = {
                                Icon(
                                    modifier = Modifier.padding(QuazzTheme.dimension.paddingS),
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = ""
                                )
                            },
                            text = stringResource(R.string.edit_quizz)
                        ) {
                            viewModel.onEvent(QuizzEvent.Edit(true))
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                        TextIconStartPosition(
                            icon = {
                                Icon(
                                    modifier = Modifier.padding(QuazzTheme.dimension.paddingS),
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            text = stringResource(R.string.delete_quizz),
                            textColor = MaterialTheme.colorScheme.error
                        ) {
                            TODO()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TextIconStartPosition(
    icon: @Composable () -> Unit,
    text: String,
    textColor: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(
            modifier = Modifier.padding(QuazzTheme.dimension.paddingM),
            color = textColor,
            text = text)
    }
}

@Composable
fun QuizzContent(
    modifier: Modifier = Modifier,
    state: QuizzState,
) {
    var expandable by remember { mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(
            top = QuazzTheme.dimension.paddingM,
            start = QuazzTheme.dimension.paddingM,
            end = QuazzTheme.dimension.paddingM
        )) {
        Text(state.quizz.title, style = MaterialTheme.typography.headlineSmall)
        Text(state.quizz.description)

        TextButton(onClick = { expandable= !expandable }, contentPadding = PaddingValues()) {
            Text(stringResource(R.string.see_question))
        }
        AnimatedVisibility(visible = expandable) {
            Column {
                state.quizz.questionnaires.forEachIndexed {
                        index, questionnaire ->
                    when(questionnaire.asString()) {
                        stringResource(id = R.string.choice_question) -> DisplayChoiceQuestion(questionnaire as Questionnaire.ChoiceQuestion, index)
                        stringResource(id = R.string.text_question)  -> DisplayTextEntryQuestion(questionnaire as Questionnaire.TextEntryQuestion, index)
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayChoiceQuestion(questionnaire: Questionnaire.ChoiceQuestion, index: Int) {
    var expandable by remember { mutableStateOf(false) }
    Column {
        DisplayQuestion(index, questionnaire.question)
        TextButton(onClick = { expandable= !expandable }, contentPadding = PaddingValues()) {
            Text(stringResource(R.string.see_answer))
        }
        AnimatedVisibility(visible = expandable) {
            Column {
                for ((indexOptions, answer) in questionnaire.answer.withIndex()) {
                    val options = questionnaire.options[indexOptions]
                    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))
                    Text(
                        buildAnnotatedString {
                            withStyle(style = paragraphStyle) {
                                append("\u2022")
                                append("\t\t")
                                append(options)
                            }
                        },
                        color = if (answer) { MaterialTheme.colorScheme.primary } else {
                            LocalContentColor.current }
                    )
                }
            }
        }
    }
}

@Composable
private fun DisplayQuestion(
    index: Int,
    questionnaire: String
) {
    Text(stringResource(R.string.question_number) + index.inc() + ": ")
    Text(questionnaire)
}

@Composable
private fun DisplayTextEntryQuestion(questionnaire: Questionnaire.TextEntryQuestion, index: Int) {
    var expandable by remember { mutableStateOf(false) }
    Column {
        DisplayQuestion(index, questionnaire.question)

        TextButton(onClick = { expandable= !expandable }, contentPadding = PaddingValues()) {
            if (expandable) {
                Text(questionnaire.answer, color = MaterialTheme.colorScheme.primary)

            } else {
                Text(stringResource(R.string.see_answer))

            }
        }
    }
}

@Composable
@PreviewLightDark
private fun QuizzContentPreview() {
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
        QuizzContent(
            state = QuizzState(
                quizz = Quizz(
                id = "id",
                title = "title",
                description = "description",
                questionnaires = questionnaires
            )),
        )
    }
}