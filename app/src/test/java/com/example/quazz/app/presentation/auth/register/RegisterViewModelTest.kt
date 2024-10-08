package com.example.quazz.app.presentation.auth.register

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.SignUpUseCase
import com.example.quazz.app.domain.validator.ConfirmPasswordValidator
import com.example.quazz.app.domain.validator.EmailValidator
import com.example.quazz.app.domain.validator.PasswordValidator
import com.example.quazz.app.domain.validator.PseudoValidator
import com.example.quazz.app.presentation.UiText
import com.example.quazz.app.presentation.ViewModelTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class RegisterViewModelTest: ViewModelTest() {
    private val mockSignUpUseCase = mockk<SignUpUseCase>(relaxed = true)
    private val passwordValidator = mockk<PasswordValidator>(relaxed = true)
    private val confirmPasswordValidator = mockk<ConfirmPasswordValidator>(relaxed = true)
    private val emailValidator = mockk<EmailValidator>(relaxed = true)
    private val pseudoValidator = mockk<PseudoValidator>(relaxed = true)

    private lateinit var viewModel: RegisterViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        viewModel = spyk(
            RegisterViewModel(
                mockSignUpUseCase,
                passwordValidator,
                confirmPasswordValidator,
                emailValidator,
                pseudoValidator
            )
        )
    }

    @AfterEach
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    @Nested
    @DisplayName("Update state")
    inner class UpdateState {
        @Test
        @DisplayName("GIVEN email THEN update email")
        fun updateEmail() {
            // GIVEN
            val email = "email"
            // WHEN
            viewModel.onEvent(RegisterEvent.UpdateEmail(email))
            // THEN
            Assertions.assertEquals(viewModel.uiState.value.email, email)
        }

        @Test
        @DisplayName("GIVEN password THEN update password")
        fun updatePassword() {
            // GIVEN
            val password = "password"
            // WHEN
            viewModel.onEvent(RegisterEvent.UpdatePassword(password))
            // THEN
            Assertions.assertEquals(viewModel.uiState.value.password, password)
        }

        @Test
        @DisplayName("GIVEN confirm password THEN update confirm password")
        fun updateConfirmPassword() {
            // GIVEN
            val confirmPassword = "confirmPassword"
            // WHEN
            viewModel.onEvent(RegisterEvent.UpdateConfirmPassword(confirmPassword))

            // THEN
            Assertions.assertEquals(viewModel.uiState.value.confirmPassword, confirmPassword)
        }
        @Test
        @DisplayName("GIVEN pseudo  THEN pseudo")
        fun updatePseudo() {
            // GIVEN
            val pseudo = "pseudo"
            // WHEN
            viewModel.onEvent(RegisterEvent.UpdatePseudo(pseudo))

            // THEN
            Assertions.assertEquals(viewModel.uiState.value.pseudo, pseudo)
        }
    }

    @Nested
    @DisplayName("Clear error")
    inner class ClearError {
        @Test
        fun clearPasswordError() {
            // WHEN
            viewModel.onEvent(RegisterEvent.ClearPasswordError)
            // THEN
            Assertions.assertEquals(viewModel.uiState.value.passwordError, UiText.DynamicString(""))
        }

        @Test
        fun clearEmailError() {
            // WHEN
            viewModel.onEvent(RegisterEvent.ClearEmailError)
            // THEN
            Assertions.assertEquals(viewModel.uiState.value.emailError, UiText.DynamicString(""))
        }

        @Test
        fun clearConfirmPasswordError() {
            // WHEN
            viewModel.onEvent(RegisterEvent.ClearConfirmPasswordError)
            // THEN
            Assertions.assertEquals(viewModel.uiState.value.confirmPasswordError, UiText.DynamicString(""))
        }

        @Test
        fun clearPseudoError() {
            // WHEN
            viewModel.onEvent(RegisterEvent.ClearPseudoError)
            // THEN
            Assertions.assertEquals(viewModel.uiState.value.pseudoError, UiText.DynamicString(""))
        }

        @Test
        fun clearSignUpError() {
            // WHEN
            viewModel.onEvent(RegisterEvent.ClearSignUpError)
            // THEN
            Assertions.assertEquals(viewModel.uiState.value.signUpError, UiText.DynamicString(""))
        }
    }

    @Test
    @DisplayName("When sign up click Then success")
    fun onSignUpClick() = runTest {
        // GIVEN
        val email = "email@example.com"
        every { emailValidator.execute(any()) } returns Result.Success(Unit)
        every { passwordValidator.execute(any()) } returns Result.Success(Unit)
        every { confirmPasswordValidator.execute(any(), any()) } returns Result.Success(Unit)
        every { pseudoValidator.execute(any()) } returns Result.Success(Unit)
        coEvery { mockSignUpUseCase.invoke(any(), any(), any()) } returns Result.Success(Unit)

        viewModel.onEvent(RegisterEvent.UpdateEmail(email))

        // WHEN
        viewModel.onEvent(RegisterEvent.SignUp)

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            mockSignUpUseCase.invoke(any(), email, any())
            viewModel.updateLoading(false)
        }
        Assertions.assertEquals(viewModel.uiState.value.isLoading, false)
        Assertions.assertEquals(viewModel.uiState.value.email, "")
        Assertions.assertEquals(viewModel.uiState.value.password, "")
        Assertions.assertEquals(viewModel.uiState.value.pseudo, "")
        Assertions.assertEquals(viewModel.uiState.value.confirmPassword, "")
    }

    @Test
    @DisplayName("When sign up click with invalid email")
    fun onSignUpClickError() = runTest {
        // GIVEN
        val email = "email@example.com"
        every { emailValidator.execute(any()) } returns Result.Success(Unit)
        every { passwordValidator.execute(any()) } returns Result.Success(Unit)
        every { confirmPasswordValidator.execute(any(), any()) } returns Result.Success(Unit)
        every { pseudoValidator.execute(any()) } returns Result.Success(Unit)
        coEvery { mockSignUpUseCase.invoke(any(), any(), any()) } returns Result.Error(DataError.Network.UNKNOWN)

        viewModel.onEvent(RegisterEvent.UpdateEmail("email@example.com"))

        // WHEN
        viewModel.onEvent(RegisterEvent.SignUp)

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            mockSignUpUseCase.invoke(any(), email, any())
            viewModel.updateLoading(false)
        }
        Assertions.assertEquals(viewModel.uiState.value.email, email)
        Assertions.assertEquals(viewModel.uiState.value.password, "")
        Assertions.assertEquals(viewModel.uiState.value.confirmPassword, "")
        Assertions.assertEquals(viewModel.uiState.value.pseudo, "")
        Assertions.assertNotEquals(viewModel.uiState.value.signUpError, "")
    }
}