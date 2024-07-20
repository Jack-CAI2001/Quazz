package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class PasswordUseCaseTest {
    private val mockAuthRepository = mockk<AuthRepository>(relaxed = true)
    private val userCase = PasswordUseCase(mockAuthRepository)
    private val oldPassword = ""
    private val newPassword = ""

    @Test
    fun success() = runTest {
        // GIVEN
        coEvery {
            mockAuthRepository.updatePassword(any(), any())
        } returns Result.Success(mockk())
        // WHEN
        userCase.invoke(oldPassword, newPassword)
        // THEN
        coVerify(exactly = 1) {
            mockAuthRepository.updatePassword(any(), any())
        }
    }

    @Test
    fun failure() = runTest {
        // GIVEN
        coEvery {
            mockAuthRepository.updatePassword(any(), any())
        } returns Result.Error(mockk())
        // WHEN
        userCase.invoke(oldPassword, newPassword)
        // THEN
        coVerify(exactly = 1) {
            mockAuthRepository.updatePassword(any(), any())
        }
    }
}