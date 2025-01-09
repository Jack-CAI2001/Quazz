package com.example.quazz.app.presentation.quizzList.runQuizz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.quazz.app.model.CustomNavType
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.model.Response
import com.example.quazz.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class RunQuizzViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val quizz = savedStateHandle.toRoute<Route.RunQuizzRoute>(typeMap = mapOf(
        typeOf<Quizz>() to CustomNavType.QuizzType
    ))

    private val _uiState = MutableStateFlow(RunQuizzState())
    val uiState: StateFlow<RunQuizzState> = _uiState.asStateFlow()

    init {
        updateQuizz(quizz.quizz)
        val responses = mutableListOf<Response>()
        quizz.quizz.questionnaires.forEach {
            questionnaire ->
            when(questionnaire) {
                is Questionnaire.ChoiceQuestion -> {
                    responses.add(Response.BooleanList(MutableList(questionnaire.options.size, {false})))
                }
                is Questionnaire.TextEntryQuestion -> {
                    responses.add(Response.StringItem(""))
                }
            }
        }
        updateResponses(responses)
    }

    fun onEvent(event: RunQuizzEvent) {
        when (event) {
            is RunQuizzEvent.UpdateStringItem -> {
                val responses = _uiState.value.responses.toMutableList()
                responses[event.indexQuestion] = Response.StringItem(event.response)
                updateResponses(responses.toList())
            }

            is RunQuizzEvent.UpdateBooleanList -> {
                val responses = _uiState.value.responses.toMutableList()
                val booleanList = (responses[event.indexQuestion] as Response.BooleanList).value.toMutableList()
                booleanList[event.indexOption] = booleanList[event.indexOption].not()
                responses[event.indexQuestion] = Response.BooleanList(booleanList)
                updateResponses(responses.toList())
            }

            RunQuizzEvent.OnComplete -> {
                updateResult(emptyList())
                _uiState.value.responses.forEachIndexed { index, response ->
                    when(response) {
                        is Response.BooleanList -> {
                            val questionnaire = _uiState.value.quizz.questionnaires[index] as Questionnaire.ChoiceQuestion
                            if (questionnaire.answer.zip(response.value).all { (correct, user) -> correct == user }.not()) {
                                val tempResult = _uiState.value.result.toMutableList()
                                tempResult.add(index)
                                updateResult(tempResult.toList())
                            }
                        }
                        is Response.StringItem -> {
                            val questionnaire = _uiState.value.quizz.questionnaires[index] as Questionnaire.TextEntryQuestion
                            if (questionnaire.answer.contains(response.value, ignoreCase = true).not() || response.value.isBlank()) {
                                val tempResult = _uiState.value.result.toMutableList()
                                tempResult.add(index)
                                updateResult(tempResult.toList())
                            }
                        }
                    }
                }
            }
        }
    }

    // region update state

    private fun updateQuizz(quizz: Quizz) {
        _uiState.value = _uiState.value.copy(quizz = quizz)
    }

    private fun updateResponses(responses: List<Response>) {
        _uiState.value = _uiState.value.copy(responses = responses)
    }

    private fun updateResult(result: List<Int>) {
        _uiState.value = _uiState.value.copy(result = result)
    }
}