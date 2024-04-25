package com.example.quazz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.quazz.ui.theme.QuazzTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuazzTheme {
                Greeting("Android", quizItems = quizItems)
            }
        }
    }
}

sealed class QuizItem {
    data class MultipleChoiceQuestion(
        val question: String,
        val options: List<String>,
        val correctAnswerIndices: List<Int>
    ) : QuizItem()

    data class TextEntryQuestion(
        val question: String,
        val answer: String
    ) : QuizItem()
}

val quizItems = listOf(
    QuizItem.MultipleChoiceQuestion(
        question = "Quelle est la capitale de la France ?",
        options = listOf("Paris", "Londres", "Berlin", "Madrid"),
        correctAnswerIndices = listOf(0)
    ),
    QuizItem.TextEntryQuestion(
        question = "Quelle est votre couleur préférée ?",
        answer = "Bleu"
    ),
    QuizItem.MultipleChoiceQuestion(
        question = "Quelles sont les couleurs primaires ?",
        options = listOf("Rouge", "Bleu", "Vert", "Jaune"),
        correctAnswerIndices = listOf(0, 1, 3)
    ),
    QuizItem.MultipleChoiceQuestion(
        question = "Quels sont les continents du monde ?",
        options = listOf("Asie", "Europe", "Afrique", "Amérique", "Océanie", "Antarctique"),
        correctAnswerIndices = listOf(0, 1, 2, 3, 4)
    ),
    QuizItem.TextEntryQuestion(
        question = "Quelle est la capitale de l'Espagne ?",
        answer = "Madrid"
    ),
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, quizItems: List<QuizItem>) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (topBar, quizz, bottomButton) = createRefs()
        val pagerState = rememberPagerState(pageCount = {
            quizItems.size
        })
        Row(
            modifier = Modifier
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                }
                .background(color = Color.Green)
        ) {
            Text(
                text = "Page count:" + pagerState.pageCount
            )
            Text(
                text = "Current page:" + pagerState.currentPage
            )
        }
        HorizontalPager(
            modifier = Modifier
                .constrainAs(quizz) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomButton.top)
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                }
                .background(color = Color.Blue),
            verticalAlignment = Alignment.Top,
            state = pagerState
        ) { page ->
            val quizItem = quizItems[page]
            when(quizItem) {
                is QuizItem.MultipleChoiceQuestion -> {
                    Column {
                        TextTitle(title = quizItem.question)
                        SubText(title = if (quizItem.correctAnswerIndices.size == 1) "Réponse unique" else "Réponses multiples")
                        LazyColumn {
                            items(quizItem.options.size) {
                                    index -> Text(text = quizItem.options[index])
                            }
                        }
                    }
                }
                is QuizItem.TextEntryQuestion -> {
                    Column {
                        var text by remember { mutableStateOf(TextFieldValue("")) }
                        TextTitle(title = quizItem.question)
                        SubText(title = "veuillez rediger votre réponse")
                        TextField(
                            value = text,
                            onValueChange = { newText ->
                                text = newText
                            }
                        )
                    }
                }

            }

            Column(modifier = Modifier.background(color = Color.Yellow)){
            }
        }
        val coroutineScope = rememberCoroutineScope()
        Row(modifier = Modifier
            .constrainAs(bottomButton) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            }
            .background(color = Color.Red)
            .height(IntrinsicSize.Min)) {
            Button(
                enabled = pagerState.currentPage != 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage -1)
                    }
                }) {
                Text("Previous")
            }
            Button(
                enabled = pagerState.currentPage != pagerState.pageCount - 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage +1)
                    }
                }) {
                Text("Next")
            }
        }
    }
}

@Composable
fun TextTitle(title: String, style: TextStyle = MaterialTheme.typography.titleLarge) {
    Text(text = title, style = style)
}

@Composable
fun SubText(title: String, style: TextStyle = MaterialTheme.typography.titleSmall) {
    Text(text = title, style = style)
}

@Composable
fun OptionText() {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
        Greeting("Android", quizItems = quizItems)
}