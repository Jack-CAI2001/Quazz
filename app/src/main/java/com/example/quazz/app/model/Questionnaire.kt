package com.example.quazz.app.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.quazz.R

sealed class Questionnaire {
    @Composable
    fun asString(): String {
        return when (this) {
            is ChoiceQuestion -> LocalContext.current.getString(R.string.choice_question)
            is TextEntryQuestion -> LocalContext.current.getString(R.string.text_question)
        }
    }
    data class ChoiceQuestion(
        val question: String = "",
        val options: List<String> = emptyList(),
        val answer: List<Boolean> = emptyList()
    ) : Questionnaire()
    data class TextEntryQuestion(
        val question: String = "",
        val answer: String = ""
    ) : Questionnaire()
}