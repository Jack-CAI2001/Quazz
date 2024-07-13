package com.example.quazz.app.presentation.auth.login

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.SignInUseCase
import com.example.quazz.app.presentation.UiText
import com.example.quazz.app.presentation.ViewModelTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LoginViewModelTest: ViewModelTest() {
    private val signInUseCase = mockk<SignInUseCase>(relaxed = true)

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        viewModel = spyk(
            LoginViewModel(
                signInUseCase
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
            viewModel.onEvent(LoginEvent.UpdateEmail(email))
            // THEN
            assertEquals(viewModel.uiState.value.email, email)
        }

        @Test
        @DisplayName("GIVEN password THEN update password")
        fun updatePassword() {
            // GIVEN
            val password = "password"
            // WHEN
            viewModel.onEvent(LoginEvent.UpdatePassword(password))
            // THEN
            assertEquals(viewModel.uiState.value.password, password)
        }
    }

    @Nested
    @DisplayName("Clear error")
    inner class ClearError {
        @Test
        fun clearSignIn() {
            // GIVEN
            val email = "wrong_email"
            val password = "wrong_password"
            viewModel.onEvent(LoginEvent.UpdateEmail(email))
            viewModel.onEvent(LoginEvent.UpdatePassword(password))
            coEvery { signInUseCase.invoke(any(), any()) } returns Result.Error(DataError.Network.NETWORK_ERROR)
            viewModel.onEvent(LoginEvent.SignIn(openAndPopUp = { _, _ -> }))

            // WHEN
            viewModel.onEvent(LoginEvent.ClearSignInError)

            // THEN
            assertEquals(viewModel.uiState.value.signinError, UiText.DynamicString(""))
        }

        @Test
        fun clearFields()  {
            // GIVEN
            viewModel.onEvent(LoginEvent.SignIn(openAndPopUp = { _, _ -> }))

            // WHEN
            viewModel.onEvent(LoginEvent.ClearFieldsError)

            // THEN
            assertEquals(viewModel.uiState.value.fieldsError, UiText.DynamicString(""))
        }
    }

    @Test
    @DisplayName("When sign in click Then success")
    fun onSignInClick() {
        // GIVEN
        val email = "email"
        val password = "password"
        viewModel.onEvent(LoginEvent.UpdateEmail(email))
        viewModel.onEvent(LoginEvent.UpdatePassword(password))
        coEvery { signInUseCase.invoke(any(), any()) } returns Result.Success(Unit)

        // WHEN
        viewModel.onEvent(LoginEvent.SignIn(openAndPopUp = { _, _ -> }))

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            signInUseCase.invoke(email, password)
            viewModel.updateLoading(false)
        }
        assertTrue(viewModel.uiState.value.fieldsError == UiText.DynamicString(""))
        assertTrue(viewModel.uiState.value.signinError == UiText.DynamicString(""))
    }

    @Test
    @DisplayName("GIVEN wrong credentials WHEN sign in click THEN error")
    fun onSignInClickError() {
        // GIVEN
        val email = "wrong_email"
        val password = "wrong_password"
        viewModel.onEvent(LoginEvent.UpdateEmail(email))
        viewModel.onEvent(LoginEvent.UpdatePassword(password))
        coEvery { signInUseCase.invoke(any(), any()) } returns Result.Error(DataError.Network.INVALID_CREDENTIALS)

        // WHEN
        viewModel.onEvent(LoginEvent.SignIn(openAndPopUp = { _, _ -> }))

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            signInUseCase.invoke(email, password)
            viewModel.updateLoading(false)
        }
        assertTrue(viewModel.uiState.value.fieldsError == UiText.DynamicString(""))
        assertFalse(viewModel.uiState.value.signinError == UiText.DynamicString(""))
    }
    @Test
    @DisplayName("When sign in click with empty credentials")
    fun onSignInClickErrorGivenEmpty() {
        // GIVEN
        val email = ""
        val password = ""
        viewModel.onEvent(LoginEvent.UpdateEmail(email))
        viewModel.onEvent(LoginEvent.UpdatePassword(password))

        // WHEN
        viewModel.onEvent(LoginEvent.SignIn(openAndPopUp = { _, _ -> }))

        // THEN
        coVerify(exactly = 0) {
            viewModel.updateLoading(true)
            signInUseCase.invoke(email, password)
            viewModel.updateLoading(false)
        }
        assertTrue(viewModel.uiState.value.signinError == UiText.DynamicString(""))
        assertFalse(viewModel.uiState.value.fieldsError == UiText.DynamicString(""))
    }
}