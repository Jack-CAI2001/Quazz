package com.example.quazz.app.presentation.create.quizz

import com.example.quazz.app.domain.SaveQuestionnaireUseCase
import com.example.quazz.app.domain.validator.QuestionnaireValidator
import com.example.quazz.app.presentation.ViewModelTest
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class CreateQuizzViewModelViewModelTest: ViewModelTest() {
    private val mockkSaveQuestionnaireUseCase = mockk<SaveQuestionnaireUseCase>(relaxed = true)
    private val mockkQuestionnaireValidator = mockk<QuestionnaireValidator>(relaxed = true)

    private lateinit var viewModel: CreateQuizzViewModel

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        viewModel = spyk(
            CreateQuizzViewModel(
                mockkSaveQuestionnaireUseCase,
                mockkQuestionnaireValidator
            )
        )
    }

    // creation de questionnaire
    // creation de question les deux types
    // modification
    // suppression


    @AfterEach
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }
}
