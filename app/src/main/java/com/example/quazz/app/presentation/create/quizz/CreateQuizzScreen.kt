package com.example.quazz.app.presentation.create.quizz

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.R
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.presentation.auth.login.TextError
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.AddOption
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.ChoiceQuestionEvent
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.DeleteOption
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.Save
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.TextEntryQuestionEvent
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.UpdateDescription
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.UpdateOption
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.UpdateQuestion
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.UpdateTitle
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.onDelete
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.onNext
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.onPrevious
import com.example.quazz.app.presentation.create.quizz.CreateQuizzEvent.onQuestionTypeSelected
import com.example.quazz.core.components.Picker
import com.example.quazz.core.components.QuazzTopAppBar
import com.example.quazz.core.components.outlinedTextField.QuazzOutlinedTextField
import com.example.quazz.core.components.rememberPickerState
import com.example.quazz.core.components.slideInFromLeftAndSlideOutToRight
import com.example.quazz.core.components.slideInFromRightAndSlideOutToLeft
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme

@Composable
fun CreateQuizzScreen(
    popUp: () -> Unit,
    viewModel: CreateQuizzViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    CreateQuizzContent(
        popUp = popUp,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun CreateQuizzContent(
    modifier: Modifier = Modifier,
    popUp: () -> Unit,
    state: CreateQuizzState,
    onEvent: (CreateQuizzEvent) -> Unit
) {
    val isStartScreen = state.currentIndex <= -1
    val isQuestionnairesEmpty = state.questionnaires.isEmpty()
    Scaffold(
        topBar = {
                 TopAppBar(
                     isStartScreen = isStartScreen,
                     isQuestionnairesEmpty = isQuestionnairesEmpty,
                     popUp = popUp,
                     onDelete = { onEvent(onDelete) },
                     onSave = { onEvent(Save) })
        },
        bottomBar = {
            BottomAppBar(
                isStartScreen = isStartScreen,
                onNext = { onEvent(onNext) },
                onPrevious = { onEvent(onPrevious) }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = state.currentIndex,
            contentKey = {
                // need that because targetState went from targetState and initialState exemple: 0 -> 1, it's going to be 001000
                // it help stabilize that
                state.currentIndex
                         },
            transitionSpec = {
                if (targetState > initialState) {
                    slideInFromRightAndSlideOutToLeft()
                } else {
                    slideInFromLeftAndSlideOutToRight()
                }
            }) { currentIndex ->
            Column(
                modifier = Modifier
                    .padding(horizontal = QuazzTheme.dimension.paddingM)
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (isStartScreen) {
                    StartScreen(onEvent = onEvent, title = state.title, description = state.description, error = state.errorStart)
                } else {
                    Row {
                        Text(text = "Current index ${currentIndex+1}")
                        Log.i("CreateQuizzContent", "Current index $currentIndex")
                        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
                        Text(text = "Number of quizz ${state.questionnaires.size}")
                    }
                    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
                    TextError(errorMessage = state.errorList.getOrElse(currentIndex, ({""})))
                    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
                    QuestionTypePicker(questionnaireType = state.questionnaires[currentIndex].asString()) {
                        onEvent(onQuestionTypeSelected(it))
                    }
                    Log.i("CreateQuizzContent", "questionnaires ${state.questionnaires}")
//                        val question = state.questionnaires[state.currentIndex]
                    val question = state.questionnaires[currentIndex]
                    when (question) {
                        is Questionnaire.ChoiceQuestion -> {
                            ChoiceQuestionContent(question = question, onEvent = onEvent)
                        }
                        is Questionnaire.TextEntryQuestion -> {
                            TextEntryQuestionContent(question = question, onEvent = onEvent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(modifier: Modifier = Modifier,
                      isStartScreen: Boolean,
                      isQuestionnairesEmpty: Boolean,
                      popUp: () -> Unit,
                      onDelete: () -> Unit,
                      onSave: () -> Unit,
) {
    QuazzTopAppBar(
        actions = {
            if (!isStartScreen) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "", tint = MaterialTheme.colorScheme.error)
                }
            }
            if (!isQuestionnairesEmpty) {
                Button(onClick = onSave) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        },
        navigateUp = popUp,
        title = stringResource(id = R.string.create_quizz), // mettre un dialog pour confirmer quittez
        canNavigateBack = true)
}

@Composable
private fun BottomAppBar(modifier: Modifier = Modifier,
                         isStartScreen: Boolean,
                         onPrevious: () -> Unit,
                         onNext: () -> Unit,) {
    Column {
        HorizontalDivider(modifier = Modifier.padding(horizontal = QuazzTheme.dimension.paddingS))
        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingXS))
        val arrangement = if (!isStartScreen) { Arrangement.SpaceBetween } else { Arrangement.End }
        Row(horizontalArrangement = arrangement, modifier = Modifier.fillMaxWidth()) {
            if (!isStartScreen) {
                TextButton(onClick = onPrevious) {
                    Text(text = stringResource(id = R.string.previous))
                }
            }
            TextButton(onClick = onNext ) {
                // mettre un dialog pour confirmer creation de question
                Text(text = stringResource(id = R.string.next))
            }
        }
        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingXS))
    }
}

@Composable
private fun StartScreen(modifier: Modifier = Modifier, error: String, title: String, description: String, onEvent: (CreateQuizzEvent) -> Unit) {
    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
    TextError(errorMessage = error)

    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
    QuazzOutlinedTextField(value = title, onValueChange = {onEvent(UpdateTitle(it))},
        label = { Text(stringResource(id = R.string.title)) },
        placeholder = { Text(stringResource(id = R.string.enter_title)) })

    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
    QuazzOutlinedTextField(value = description, onValueChange = {onEvent(UpdateDescription(it))},
        label = { Text(stringResource(id = R.string.description)) },
        placeholder = { Text(text = stringResource(id = R.string.enter_description)) })
}

@Composable
fun QuestionOutlinedTextField(question: String, onQuestionChange: (String) -> Unit) {
    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
    QuazzOutlinedTextField(
        value = question,
        onValueChange = {onQuestionChange(it)},
        label = { Text(stringResource(id = R.string.enter_question)) },
        placeholder = { Text(stringResource(id = R.string.enter_question)) })
    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
}

@Composable
private fun ChoiceQuestionContent(
    modifier: Modifier = Modifier,
    question: Questionnaire.ChoiceQuestion,
    onEvent: (CreateQuizzEvent) -> Unit
) {
    QuestionOutlinedTextField(question = question.question, onQuestionChange = { onEvent(UpdateQuestion(it)) })
    // enter proposition
    Row(modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        var option by remember { mutableStateOf("") }
        QuazzOutlinedTextField(modifier = Modifier.weight(weight = 1f), value = option, onValueChange = { option = it }, label = { Text(
            stringResource(id = R.string.enter_proposition)
        )})
        TextButton(onClick = {
            onEvent(AddOption(option))
            option = "" }) {
            Text(text = stringResource(id = R.string.add))
        }
    }

    // proposition list
    val listProposition = question.options
    listProposition.forEachIndexed { index, string ->
        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            QuazzOutlinedTextField(
                modifier = Modifier.weight(weight = 1f),
                colors = question.answer[index].let {
                    if (it) {
                        OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.tertiary)
                    } else {
                        OutlinedTextFieldDefaults.colors()
                    }
                },
                value = string, onValueChange = { onEvent(UpdateOption(it, index))}, label = { Text(
                    stringResource(id = R.string.proposition)) })
            IconButton(onClick = { onEvent(ChoiceQuestionEvent.UpdateAnswer(index)) }) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "",
                    modifier = question.answer[index].let {
                        if (it) {
                            Modifier.border(2.dp, MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                        } else {
                            Modifier
                        }
                    },
                    tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = { onEvent(DeleteOption(index)) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
    Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingS))
}

@Composable
private fun TextEntryQuestionContent(
    question: Questionnaire.TextEntryQuestion,
    onEvent: (CreateQuizzEvent) -> Unit
) {
    QuestionOutlinedTextField(question = question.question, onQuestionChange = { onEvent(UpdateQuestion(it)) })
    // Answer
    QuazzOutlinedTextField(value = question.answer, onValueChange = {onEvent(TextEntryQuestionEvent.UpdateAnswer(it))}, label = { Text(
        stringResource(id = R.string.answer)) }, placeholder = { Text(stringResource(id = R.string.enter_answer)) })
}

@Composable
fun QuestionTypePicker(modifier: Modifier = Modifier, questionnaireType: String, onEvent: (Questionnaire) -> Unit) {
    val questionnaireTypeList = listOf(Questionnaire.ChoiceQuestion(), Questionnaire.TextEntryQuestion()).map { it.asString() }
    val values = remember { questionnaireTypeList }
    val valuesPickerState = rememberPickerState()
    Column(
        modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Picker(
                state = valuesPickerState,
                items = values,
                visibleItemsCount = 3,
                startIndex = values.indexOf(questionnaireType),
                modifier = Modifier.weight(0.3f),
                textModifier = Modifier.padding(QuazzTheme.dimension.paddingS),
                textStyle = MaterialTheme.typography.titleLarge
            )
        }
    }
    if (valuesPickerState.selectedItem.isNotBlank()) {
        val questionnaireTypeSelected = when (valuesPickerState.selectedItem) {
            stringResource(id = R.string.choice_question) -> Questionnaire.ChoiceQuestion()
            stringResource(id = R.string.text_question)  -> Questionnaire.TextEntryQuestion()
            else -> Questionnaire.ChoiceQuestion()
        }
        LaunchedEffect(questionnaireTypeSelected) {
            onEvent(questionnaireTypeSelected)
        }
    }
}
@Composable
@PreviewLightDark
private fun CreateQuizzContentPreview() {
    AppTheme {
        CreateQuizzContent(
            state = CreateQuizzState(
                currentIndex = 1,
                questionnaires =
                listOf(
                    Questionnaire.ChoiceQuestion(
                        question = "Which country is in Europe",
                        options = listOf("France", "Germany", "USA"),
                        answer = listOf(true, true, false)
                    ),
                    Questionnaire.ChoiceQuestion(
                        question = "What is the capital of France?",
                        options = listOf("Paris", "London", "Berlin"),
                        answer = listOf(true, false, false)
                    ),
                    Questionnaire.TextEntryQuestion(
                        question = "What is the capital of France?",
                        answer = "Paris"
                    )
                )
            ),
            onEvent = {},
            popUp = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun Color() {
    AppTheme {
        val colors = MaterialTheme.colorScheme
//        val primary: Color,
//        val onPrimary: Color,
//        val primaryContainer: Color,
//        val onPrimaryContainer: Color,
//        val inversePrimary: Color,
//        val secondary: Color,
//        val onSecondary: Color,
//        val secondaryContainer: Color,
//        val onSecondaryContainer: Color,
//        val tertiary: Color,
//        val onTertiary: Color,
//        val tertiaryContainer: Color,
//        val onTertiaryContainer: Color,
//        val background: Color,
//        val onBackground: Color,
//        val surface: Color,
//        val onSurface: Color,
//        val surfaceVariant: Color,
//        val onSurfaceVariant: Color,
//        val surfaceTint: Color,
//        val inverseSurface: Color,
//        val inverseOnSurface: Color,
//        val error: Color,
//        val onError: Color,
//        val errorContainer: Color,
//        val onErrorContainer: Color,
//        val outline: Color,
//        val outlineVariant: Color,
//        val scrim: Color,
//        val surfaceBright: Color,
//        val surfaceDim: Color,
//        val surfaceContainer: Color,
//        val surfaceContainerHigh: Color,
//        val surfaceContainerHighest: Color,
//        val surfaceContainerLow: Color,
//        val surfaceContainerLowest: Color,
        LazyColumn {
            item {
                OutlinedTextField(value = "primary", onValueChange = {}, modifier = Modifier.background(colors.primary))
                OutlinedTextField(value = "onPrimary", onValueChange = {}, modifier = Modifier.background(colors.onPrimary))
                OutlinedTextField(value = "primaryContainer", onValueChange = {}, modifier = Modifier.background(colors.primaryContainer))
                OutlinedTextField(value = "onPrimaryContainer", onValueChange = {}, modifier = Modifier.background(colors.onPrimaryContainer))
                OutlinedTextField(value = "inversePrimary", onValueChange = {}, modifier = Modifier.background(colors.inversePrimary))
                OutlinedTextField(value = "secondary", onValueChange = {}, modifier = Modifier.background(colors.secondary))
                OutlinedTextField(value = "onSecondary", onValueChange = {}, modifier = Modifier.background(colors.onSecondary))
                OutlinedTextField(value = "secondaryContainer", onValueChange = {}, modifier = Modifier.background(colors.secondaryContainer))
                OutlinedTextField(value = "onSecondaryContainer", onValueChange = {}, modifier = Modifier.background(colors.onSecondaryContainer))
                OutlinedTextField(value = "tertiary", onValueChange = {}, modifier = Modifier.background(colors.tertiary))
                OutlinedTextField(value = "onTertiary", onValueChange = {}, modifier = Modifier.background(colors.onTertiary))
                OutlinedTextField(value = "tertiaryContainer", onValueChange = {}, modifier = Modifier.background(colors.tertiaryContainer))
                OutlinedTextField(value = "onTertiaryContainer", onValueChange = {}, modifier = Modifier.background(colors.onTertiaryContainer))
                OutlinedTextField(value = "background", onValueChange = {}, modifier = Modifier.background(colors.background))
                OutlinedTextField(value = "onBackground", onValueChange = {}, modifier = Modifier.background(colors.onBackground))
                OutlinedTextField(value = "surface", onValueChange = {}, modifier = Modifier.background(colors.surface))
                OutlinedTextField(value = "onSurface", onValueChange = {}, modifier = Modifier.background(colors.onSurface))
                OutlinedTextField(value = "surfaceVariant", onValueChange = {}, modifier = Modifier.background(colors.surfaceVariant))
                OutlinedTextField(value = "onSurfaceVariant", onValueChange = {}, modifier = Modifier.background(colors.onSurfaceVariant))
                OutlinedTextField(value = "surfaceTint", onValueChange = {}, modifier = Modifier.background(colors.surfaceTint))
                OutlinedTextField(value = "inverseSurface", onValueChange = {}, modifier = Modifier.background(colors.inverseSurface))
                OutlinedTextField(value = "inverseOnSurface", onValueChange = {}, modifier = Modifier.background(colors.inverseOnSurface))
                OutlinedTextField(value = "error", onValueChange = {}, modifier = Modifier.background(colors.error))
                OutlinedTextField(value = "onError", onValueChange = {}, modifier = Modifier.background(colors.onError))
                OutlinedTextField(value = "errorContainer", onValueChange = {}, modifier = Modifier.background(colors.errorContainer))
                OutlinedTextField(value = "onErrorContainer", onValueChange = {}, modifier = Modifier.background(colors.onErrorContainer))
                OutlinedTextField(value = "outline", onValueChange = {}, modifier = Modifier.background(colors.outline))
                OutlinedTextField(value = "outlineVariant", onValueChange = {}, modifier = Modifier.background(colors.outlineVariant))
                OutlinedTextField(value = "scrim", onValueChange = {}, modifier = Modifier.background(colors.scrim))
                OutlinedTextField(value = "surfaceBright", onValueChange = {}, modifier = Modifier.background(colors.surfaceBright))
                OutlinedTextField(value = "surfaceDim", onValueChange = {}, modifier = Modifier.background(colors.surfaceDim))
                OutlinedTextField(value = "surfaceContainer", onValueChange = {}, modifier = Modifier.background(colors.surfaceContainer))
                OutlinedTextField(value = "surfaceContainerHigh", onValueChange = {}, modifier = Modifier.background(colors.surfaceContainerHigh))
                OutlinedTextField(value = "surfaceContainerHighest", onValueChange = {}, modifier = Modifier.background(colors.surfaceContainerHighest))
                OutlinedTextField(value = "surfaceContainerLow", onValueChange = {}, modifier = Modifier.background(colors.surfaceContainerLow))
                OutlinedTextField(value = "surfaceContainerLowest", onValueChange = {}, modifier = Modifier.background(colors.surfaceContainerLowest))
            }
        }
    }

}