package com.example.quazz.app.presentation.quizzList.createQuizz

import com.example.quazz.app.domain.SaveQuestionnaireUseCase
import com.example.quazz.app.domain.validator.QuestionnaireValidator
import com.example.quazz.app.presentation.ViewModelTest
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class QuizzListViewModelTest: ViewModelTest() {
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

    @AfterEach
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }
}
