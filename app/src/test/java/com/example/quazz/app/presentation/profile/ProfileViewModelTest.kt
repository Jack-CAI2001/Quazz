package com.example.quazz.app.presentation.profile

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.GetUserUseCase
import com.example.quazz.app.domain.LogOutUseCase
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.UpdateUserUseCase
import com.example.quazz.app.domain.UserConnectedUseCase
import com.example.quazz.app.model.User
import com.example.quazz.app.presentation.UiText
import com.example.quazz.app.presentation.ViewModelTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProfileViewModelTest: ViewModelTest() {
    private val mockkUserConnectedUseCase = mockk<UserConnectedUseCase>(relaxed = true)
    private val mockkUpdateUserUseCase = mockk<UpdateUserUseCase>(relaxed = true)
    private val mockkGetUserUseCase = mockk<GetUserUseCase>(relaxed = true)
    private val mockkLogOutUseCase = mockk<LogOutUseCase>(relaxed = true)
    private lateinit var viewModel: ProfileViewModel
    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        viewModel = spyk(
            ProfileViewModel(
                mockkUserConnectedUseCase,
                mockkUpdateUserUseCase,
                mockkGetUserUseCase,
                mockkLogOutUseCase,
            )
        )
    }

    @AfterEach
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    @Test
    fun getUserSuccess() {
        // GIVEN
        coEvery {
            mockkGetUserUseCase.invoke()
        } returns Result.Success(mockk<User>())

        // WHEN
        viewModel.getUser()

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            mockkGetUserUseCase.invoke()
            viewModel.updateLoading(false)
        }
        assertEquals(viewModel.uiState.value.errorMessage, UiText.DynamicString(""))
    }

    @Test
    fun getUserFail() {
        // GIVEN
        coEvery {
            mockkGetUserUseCase.invoke()
        } returns Result.Error(DataError.Network.UNKNOWN)

        // WHEN
        viewModel.getUser()

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            mockkGetUserUseCase.invoke()
            viewModel.updateLoading(false)
        }
        assertNotEquals(viewModel.uiState.value.errorMessage, UiText.DynamicString(""))
    }

    @Test
    fun logOut() {
        // GIVEN
        every { mockkLogOutUseCase.invoke() } returns Unit
        viewModel.updateIsUserConnected(true)

        // WHEN
        viewModel.onEvent(ProfileEvent.LogOut)
        // THEN
        assertEquals(viewModel.uiState.value.isUserConnected, false)
    }

    @Test
    fun savePseudoSuccess() {
        // GIVEN
        coEvery { mockkUpdateUserUseCase.invoke(any()) } returns Result.Success(Unit)
        viewModel.onEvent(ProfileEvent.UpdatePseudo("pseudo"))
        // WHEN
        viewModel.onEvent(ProfileEvent.SavePseudo)

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            mockkUpdateUserUseCase.invoke(any())
            viewModel.updateLoading(false)
        }
        assertEquals(viewModel.uiState.value.user.pseudo, "pseudo")
        assertEquals(viewModel.uiState.value.pseudoText, "")
        assertEquals(viewModel.uiState.value.errorMessage, UiText.DynamicString(""))
    }

    @Test
    fun savePseudoFail() {
        // GIVEN
        coEvery { mockkUpdateUserUseCase.invoke(any()) } returns Result.Error(DataError.Network.UNKNOWN)
        viewModel.onEvent(ProfileEvent.UpdatePseudo("pseudo"))

        // WHEN
        viewModel.onEvent(ProfileEvent.SavePseudo)

        // THEN
        coVerifyOrder {
            viewModel.updateLoading(true)
            mockkUpdateUserUseCase.invoke(any())
            viewModel.updateLoading(false)
        }
        assertEquals(viewModel.uiState.value.user.pseudo, "")
        assertEquals(viewModel.uiState.value.pseudoText, "")
        assertNotEquals(viewModel.uiState.value.errorMessage, UiText.DynamicString(""))
    }

}