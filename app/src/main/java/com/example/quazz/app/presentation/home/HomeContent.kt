package com.example.quazz.app.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.quazz.app.presentation.model.QuizItem
import com.example.quazz.core.components.SubText
import com.example.quazz.core.components.TextTitle
import kotlinx.coroutines.launch

val quizItems = listOf(
    QuizItem.MultipleChoiceQuestion(
        question = "Quelle est la capitale de la France ?",
        options = listOf("Paris", "Londres", "Berlin", "Madrid"),
        correctAnswer = listOf("Paris")
    ),
    QuizItem.TextEntryQuestion(
        question = "Quelle est votre couleur préférée ?",
        answer = "Bleu"
    ),
    QuizItem.MultipleChoiceQuestion(
        question = "Quelles sont les couleurs primaires ?",
        options = listOf("Rouge", "Bleu", "Vert", "Jaune"),
        correctAnswer = listOf("Rouge", "Bleu", "Jaune")
    ),
    QuizItem.MultipleChoiceQuestion(
        question = "Quels sont les continents du monde ?",
        options = listOf("Asie", "Europe", "Afrique", "Amérique", "Océanie", "Antarctique"),
        correctAnswer = listOf("Asie", "Europe", "Afrique", "Amérique", "Océanie", "Antarctique")
    ),
    QuizItem.TextEntryQuestion(
        question = "Quelle est la capitale de l'Espagne ?",
        answer = "Madrid"
    ),
)

//var quizAnswers = mutableMapOf<Int, Any>()

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Quiz(modifier: Modifier = Modifier, quizItems: List<QuizItem>) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (topBar, quiz, bottomButton) = createRefs()
        val pagerState = rememberPagerState(pageCount = {
            quizItems.size
        })
        QuizHeader(
            Modifier
            .constrainAs(topBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            }
            .padding(16.dp),
            pagerState
        )
        QuizBody(modifier = Modifier
            .constrainAs(quiz) {
                top.linkTo(topBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(bottomButton.top)
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
            },
            pagerState)
        QuizBottom(modifier =
        Modifier
            .constrainAs(bottomButton) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            }
            .height(IntrinsicSize.Min)
            .padding(16.dp),
            pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizHeader(modifier: Modifier, pagerState: PagerState) {
    Column(
        modifier = modifier
    ) {
        Row {
            Text(
                text = "Page count:" + pagerState.pageCount
            )
            Text(
                text = "Current page:" + pagerState.currentPage
            )
        }
        val animatedProgress = animateFloatAsState(
            targetValue = pagerState.currentPage.toFloat() / (pagerState.pageCount-1),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier.fillMaxWidth(),)
    }
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun QuizBody(modifier: Modifier, pagerState: PagerState) {
    val quizAnswers = remember { mutableMapOf<Int, Any>() }
    HorizontalPager(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        state = pagerState,
    ) { page ->
        when(val quizItem = quizItems[page]) {
            is QuizItem.MultipleChoiceQuestion -> {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    TextTitle(title = quizItem.question)
                    SubText(title = if (quizItem.correctAnswer.size == 1) "Réponse unique" else "Réponses multiples")
                    if (quizItem.correctAnswer.size == 1) {
                        var selectedOption by remember { mutableStateOf<String?>(null) }
                        quizAnswers[pagerState.currentPage] = selectedOption ?: ""
                        LazyColumn(modifier = Modifier.padding(16.dp)) {
                            items(quizItem.options) { option ->
                                OutlinedAnswerButton(
                                    text = option,
                                    isSelected = option == selectedOption,
                                    onSelect = { selectedOption = option }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Text(text = "Selected item: $selectedOption", modifier = Modifier.padding(top = 16.dp))
                    } else {
                        val selectedOptions = remember { mutableStateListOf<String>() }
                        quizAnswers[pagerState.currentPage] = selectedOptions.toList()
                        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                            items(quizItem.options) {
                                    option ->
                                OutlinedAnswerButton(
                                    text = option,
                                    isSelected = selectedOptions.contains(option),
                                    onSelect = {
                                        if (selectedOptions.contains(option)) {
                                            selectedOptions.remove(option)
                                        } else {
                                            selectedOptions.add(option)
                                        }
                                    })
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Text(text = "Selected item: ${selectedOptions.toList()}", modifier = Modifier.padding(top = 16.dp))
                    }
                    Text(quizAnswers.toString())
                }
            }
            is QuizItem.TextEntryQuestion -> {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    TextTitle(title = quizItem.question)
                    SubText(title = "veuillez rediger votre réponse")
                    val focusManager = LocalFocusManager.current
                    var text by remember { mutableStateOf(TextFieldValue("")) }
                    quizAnswers[pagerState.currentPage] = text.text
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )
                    Text(quizAnswers.toString())
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizBottom(modifier: Modifier, pagerState: PagerState) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        val coroutineScope = rememberCoroutineScope()
        Button(
            modifier = Modifier.weight(1f),
            enabled = pagerState.currentPage != 0,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }) {
            Text("Previous")
        }
        val openAlertDialog = remember { mutableStateOf(false) }
        if (openAlertDialog.value) {
            QuizResultDialog(onDismissRequest = { openAlertDialog.value = false },
                onConfirm = { openAlertDialog.value = false })
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .alpha(if (pagerState.currentPage == pagerState.pageCount - 1) 1f else 0f),
            onClick = { openAlertDialog.value = true }) {
            Text("Envoyer")
        }
        Button(
            modifier = Modifier.weight(1f),
            enabled = pagerState.currentPage != pagerState.pageCount - 1,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }) {
            Text("Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedAnswerButton(text: String, isSelected: Boolean, onSelect: () -> Unit) {
    //var checkedState by remember { mutableStateOf(false) }
    OutlinedButton(
        shape = TextFieldDefaults.outlinedShape,
        modifier = Modifier.fillMaxWidth(),
        border = if (isSelected) BorderStroke(2.0.dp, MaterialTheme.colorScheme.primary) else ButtonDefaults.outlinedButtonBorder,
        onClick = onSelect ){
        Text(text)
    }
}

@Composable
fun SelectableItem(item: String, isSelected: Boolean, onSelect: () -> Unit) {
    val backgroundColor = if (isSelected) Color.LightGray else Color.Transparent
    val textColor = if (isSelected) Color.Black else Color.DarkGray
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onSelect)
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(text = item, color = textColor, fontSize = 18.sp, fontWeight = fontWeight)
    }
}

@Composable
fun QuizResultDialog(onDismissRequest: () -> Unit = {}, onConfirm: () -> Unit = {}) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("Quiz score")
        },
        text = {
            Text("This is an alert dialog")
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("OK")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Quiz(quizItems = quizItems)
}