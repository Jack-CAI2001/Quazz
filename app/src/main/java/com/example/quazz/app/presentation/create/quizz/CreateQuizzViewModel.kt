package com.example.quazz.app.presentation.create.quizz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.SaveQuestionnaireUseCase
import com.example.quazz.app.domain.asErrorUiText
import com.example.quazz.app.domain.validator.QuestionnaireValidator
import com.example.quazz.app.model.Questionnaire
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuizzViewModel @Inject constructor(
    private val saveQuestionnaireUseCase: SaveQuestionnaireUseCase,
    private val questionnaireValidator: QuestionnaireValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateQuizzState())
    val uiState: StateFlow<CreateQuizzState> = _uiState.asStateFlow()

    fun onEvent(event: CreateQuizzEvent) {
        when (event) {
            is CreateQuizzEvent.Save -> onSave()
            is CreateQuizzEvent.onDelete -> deleteQuestionnaire()
            is CreateQuizzEvent.onNext -> onNext()
            is CreateQuizzEvent.onPrevious -> updateIndex(_uiState.value.currentIndex - 1)
            is CreateQuizzEvent.onQuestionTypeSelected -> updateQuestionType(event.type)
            is CreateQuizzEvent.UpdateAuthor -> updateAuthor(event.author)
            is CreateQuizzEvent.UpdateTitle -> updateTitle(event.title)
            is CreateQuizzEvent.UpdateDescription -> _uiState.value = _uiState.value.copy(description = event.description)
            is CreateQuizzEvent.UpdateQuestion -> updateQuestion(event.question)
            is CreateQuizzEvent.AddOption -> addOption(event.option)
            is CreateQuizzEvent.DeleteOption -> deleteOption(event.index)
            is CreateQuizzEvent.UpdateOption -> updateOption(event.option, event.index)
            is CreateQuizzEvent.ChoiceQuestionEvent.UpdateAnswer -> updateAnswer(index = event.optionIndex)
            is CreateQuizzEvent.TextEntryQuestionEvent.UpdateAnswer -> updateAnswer(textAnswer = event.answer)
        }
    }

    // region update state

    private fun updateAuthor(author: String) {
        _uiState.value = _uiState.value.copy(author = author)
    }

    private fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    private fun updateQuestionnaires(questionnaires: List<Questionnaire>) {
        _uiState.value = _uiState.value.copy(questionnaires = questionnaires)
    }

    private fun updateIndex(index: Int) {
        _uiState.value = _uiState.value.copy(currentIndex = index)
    }

    private fun updateQuestionType(type: Questionnaire) {
        val currentIndex = _uiState.value.currentIndex
        val questionnaires = _uiState.value.questionnaires.toMutableList()
        if (sameType(questionnaires[currentIndex], type).not()) {
            questionnaires[currentIndex] = type
            updateQuestionnaires(questionnaires.toList())
        }
    }

    private fun sameType(question1: Questionnaire, question2: Questionnaire): Boolean {
        return when(question1) {
            is Questionnaire.ChoiceQuestion -> question2 is Questionnaire.ChoiceQuestion
            is Questionnaire.TextEntryQuestion -> question2 is Questionnaire.TextEntryQuestion
        }
    }

    private fun updateQuestion(question: String) {
        val currentIndex = _uiState.value.currentIndex
        val questionnaires = _uiState.value.questionnaires.toMutableList()
        when(questionnaires[currentIndex]) {
            is Questionnaire.ChoiceQuestion -> questionnaires[currentIndex] = (questionnaires[currentIndex] as Questionnaire.ChoiceQuestion).copy(question = question)
            is Questionnaire.TextEntryQuestion -> questionnaires[currentIndex] = (questionnaires[currentIndex] as Questionnaire.TextEntryQuestion).copy(question = question)
        }
        updateQuestionnaires(questionnaires.toList())
    }

    private fun addOption(option: String) {
        val currentIndex = _uiState.value.currentIndex
        val questionnaires = _uiState.value.questionnaires.toMutableList()
        when(questionnaires[currentIndex]) {
            is Questionnaire.ChoiceQuestion -> {
                val options = (questionnaires[currentIndex] as Questionnaire.ChoiceQuestion).options.toMutableList()
                options.add(0, option)
                val answer = (questionnaires[currentIndex] as Questionnaire.ChoiceQuestion).answer.toMutableList()
                answer.add(0, false)
                questionnaires[currentIndex] = (questionnaires[currentIndex] as Questionnaire.ChoiceQuestion).copy(options = options.toList(), answer = answer)
            }
            is Questionnaire.TextEntryQuestion -> TODO() // ya pas d'option
        }
        updateQuestionnaires(questionnaires.toList())
    }

    private fun updateOption(option: String, index: Int) {
        val currentIndex = _uiState.value.currentIndex
        val questionnaires = _uiState.value.questionnaires.toMutableList()
        when(questionnaires[currentIndex]) {
            is Questionnaire.ChoiceQuestion -> {
                val options = (questionnaires[currentIndex] as Questionnaire.ChoiceQuestion).options.toMutableList()
                options[index] = option
                questionnaires[currentIndex] = (questionnaires[currentIndex] as Questionnaire.ChoiceQuestion).copy(options = options.toList())
            }
            is Questionnaire.TextEntryQuestion -> TODO()
        }
//        whenQuestionThen(questionnaires[currentIndex],
//            isMultipleChoiceQuestion = {
//                questionnaires[currentIndex] = it.copy(options = it.options.let {
//                    it.toMutableList()[index] = option
//                    it.toList()
//                })
////                it.options.toMutableList()[index] = option
////                questionnaires[currentIndex] = it.copy(options = )
//                                       },
//            isTextEntryQuestion = { updateQuestionnaires(questionnaires.toList()) },
//            isUniqueChoiceQuestion = { updateQuestionnaires(questionnaires.toList()) }
//        )
        updateQuestionnaires(questionnaires.toList())
    }

    private fun updateAnswer(index: Int = -1, textAnswer: String = "") {
        // separer la méthode en deux ?
        val currentIndex = _uiState.value.currentIndex
        val questionnaires = _uiState.value.questionnaires.toMutableList()
        whenQuestionThen(
            questionnaires[currentIndex],
            isChoiceQuestion = { question ->
                val answer = question.answer.toMutableList()
                answer[index] = !answer[index]
                questionnaires[currentIndex] = question.copy(answer = answer.toList())
                                       },
            isTextEntryQuestion = { questionnaires[currentIndex] = it.copy(answer = textAnswer) },
        )
        updateQuestionnaires(questionnaires.toList())
    }

    private fun deleteOption(index: Int) {
        val currentIndex = _uiState.value.currentIndex
        val questionnaires = _uiState.value.questionnaires.toMutableList()
        whenQuestionThen(
            questionnaires[currentIndex],
            isChoiceQuestion = { question ->
                val options = question.options.toMutableList()
                options.removeAt(index)
                val answer = question.answer.toMutableList()
                answer.removeAt(index)
                questionnaires[currentIndex] = question.copy(options = options.toList(), answer = answer.toList())
                                       },
            isTextEntryQuestion = { updateQuestionnaires(questionnaires.toList()) },
        )
        updateQuestionnaires(questionnaires.toList())
    }

    private fun whenQuestionThen(
        question: Questionnaire,
        isChoiceQuestion: (Questionnaire.ChoiceQuestion) -> Unit,
        isTextEntryQuestion: (Questionnaire.TextEntryQuestion) -> Unit,
    ) {
        when(question) {
            is Questionnaire.ChoiceQuestion -> isChoiceQuestion(question)
            is Questionnaire.TextEntryQuestion -> isTextEntryQuestion(question)
        }
    }

    private fun deleteQuestionnaire() {
        _uiState.value = _uiState.value.copy(errorList = emptyList())
        _uiState.value = _uiState.value.copy(errorStart = "")
        val currentIndex = _uiState.value.currentIndex
        val questionnaires = _uiState.value.questionnaires.toMutableList()
        questionnaires.removeAt(currentIndex)
        updateQuestionnaires(questionnaires.toList())
        updateIndex(currentIndex - 1)
    }

    // endregion

    private fun onSave() {
        // check if every question has a question and at least one answer
        // if not redirect to the question that is missing
        // index is between -1 to inf
        val errorList = MutableList(_uiState.value.questionnaires.size) { "" }
        _uiState.value = _uiState.value.copy(errorStart = "")
        if (_uiState.value.title.isBlank() || _uiState.value.description.isBlank()) {
            _uiState.value = _uiState.value.copy(errorStart = "Remplir mon reuf")
            updateIndex(-1)
            return
        }
        _uiState.value.questionnaires.forEachIndexed { index, questionnaire ->
            when(questionnaire) {
                is Questionnaire.ChoiceQuestion -> {
                    if (questionnaire.question.isBlank() || questionnaire.options.isEmpty() || questionnaire.options.any { it.isBlank() } || questionnaire.answer.none { it }) {
                        errorList[index] = "Remplir stp"
                        _uiState.value = _uiState.value.copy(errorList = errorList.toList())
                        updateIndex(index)
                        return
                    }
                }
                is Questionnaire.TextEntryQuestion -> {
                    if (questionnaire.question.isBlank() || questionnaire.answer.isBlank()) {
                        errorList[index] = "Remplir stp"
                        _uiState.value = _uiState.value.copy(errorList = errorList.toList())
                        updateIndex(index)
                        return
                    }
                }
            }
            questionnaireValidator.execute(questionnaire).let {
                if (it is Result.Error) {
                    updateIndex(index)
                    errorList[index] = it.asErrorUiText().toString() // retirer le toString et changer en UiText
                    _uiState.value = _uiState.value.copy(errorList = errorList.toList())
                    return
                }
            }

        }

        viewModelScope.launch {
            // loading ?
            // if fail stay on screen

            when(val result = saveQuestionnaireUseCase.invoke(_uiState.value.title, _uiState.value.description, _uiState.value.questionnaires)) {
                is Result.Error -> {
                    // show snackbar ?
                }
                is Result.Success -> {
                    // return on createListScreen
                    // something
                }
            }
        }
    }

    private fun onNext() {
        val nextIndex = _uiState.value.currentIndex + 1
        updateIndex(nextIndex)
        if (nextIndex < _uiState.value.questionnaires.size) {
            updateIndex(nextIndex)
        } else {
            updateQuestionnaires(_uiState.value.questionnaires + Questionnaire.ChoiceQuestion())
            updateIndex(nextIndex)
        }
    }
}