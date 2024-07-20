package com.example.quazz.app.presentation.profile.updateProfile

import com.example.quazz.app.domain.EmailUseCase
import com.example.quazz.app.domain.PasswordUseCase
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.validator.ConfirmPasswordValidator
import com.example.quazz.app.domain.validator.EmailValidator
import com.example.quazz.app.domain.validator.PasswordValidator
import com.example.quazz.app.presentation.UiText
import com.example.quazz.app.presentation.ViewModelTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateProfileViewModelTest: ViewModelTest() {
    private val mockkEmailUseCase = mockk<EmailUseCase>(relaxed = true)
    private val mockkPasswordUseCase = mockk<PasswordUseCase>(relaxed = true)
    private val passwordValidator = mockk<PasswordValidator>(relaxed = true)
    private val confirmPasswordValidator = mockk<ConfirmPasswordValidator>(relaxed = true)
    private val emailValidator = mockk<EmailValidator>(relaxed = true)

    private lateinit var viewModel: UpdateProfileViewModel
    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        viewModel = spyk(
            UpdateProfileViewModel(
                mockkEmailUseCase,
                mockkPasswordUseCase,
                emailValidator,
                passwordValidator,
                confirmPasswordValidator
            )
        )
    }

    @AfterEach
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    @Test
    fun onSaveUpdateEmailSuccess() {
        // GIVEN
        every { emailValidator.execute(any()) } returns Result.Success(Unit)
        coEvery { mockkEmailUseCase.invoke(any(), any()) } returns Result.Success(Unit)

        // WHEN
        viewModel.onEvent(UpdateProfileEvent.Save(UpdateProfile.UpdateEmail, {}))

        // THEN
        assertEquals(viewModel.uiState.value.email, "")
        assertEquals(viewModel.uiState.value.currentPassword, "")
        assertEquals(viewModel.uiState.value.errorMessage, UiText.DynamicString(""))
    }

    @Test
    fun onSaveUpdateEmailFail() {
        // GIVEN
        every { emailValidator.execute(any()) } returns Result.Success(Unit)
        coEvery { mockkEmailUseCase.invoke(any(), any()) } returns Result.Error(mockk())
        viewModel.onEvent(UpdateProfileEvent.UpdateEmail("test@example.com"))

        // WHEN
        viewModel.onEvent(UpdateProfileEvent.Save(UpdateProfile.UpdateEmail, {}))

        // THEN
        coVerify {
            mockkEmailUseCase.invoke(any(), any())
        }
        assertNotEquals(viewModel.uiState.value.errorMessage, "")
        assertEquals(viewModel.uiState.value.email, "test@example.com")
    }

    @Test
    fun onSaveUpdatePasswordSuccess() {
        // GIVEN
        every { passwordValidator.execute(any()) } returns Result.Success(Unit)
        every { confirmPasswordValidator.execute(any(), any()) } returns Result.Success(Unit)
        coEvery { mockkPasswordUseCase.invoke(any(), any()) } returns Result.Success(Unit)

        // WHEN
        viewModel.onEvent(UpdateProfileEvent.Save(UpdateProfile.UpdatePassword, {}))

        // THEN
        assertEquals(viewModel.uiState.value.newPassword, "")
        assertEquals(viewModel.uiState.value.currentPassword, "")
        assertEquals(viewModel.uiState.value.confirmPassword, "")
        assertEquals(viewModel.uiState.value.errorMessage, UiText.DynamicString(""))
    }

    @Test
    fun onSaveUpdatePasswordFail() {
        // GIVEN
        every { passwordValidator.execute(any()) } returns Result.Success(Unit)
        every { confirmPasswordValidator.execute(any(), any()) } returns Result.Success(Unit)
        coEvery { mockkPasswordUseCase.invoke(any(), any()) } returns Result.Error(mockk())

        // WHEN
        viewModel.onEvent(UpdateProfileEvent.Save(UpdateProfile.UpdatePassword, {}))

        // THEN
        assertEquals(viewModel.uiState.value.newPassword, "")
        assertEquals(viewModel.uiState.value.currentPassword, "")
        assertEquals(viewModel.uiState.value.confirmPassword, "")
        assertNotEquals(viewModel.uiState.value.errorMessage, "")
    }
}