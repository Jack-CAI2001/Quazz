package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class EmailUseCaseTest {
    private val mockAuthRepository = mockk<AuthRepository>(relaxed = true)
    private val emailUseCase = EmailUseCase(mockAuthRepository)
    private val email = ""
    private val userPassword = ""
    @Test
    fun success() = runTest{
        // GIVEN
        coEvery {
            mockAuthRepository.updateEmail(any(), any())
        } returns Result.Success(Unit)

        // WHEN
        emailUseCase.invoke(email, userPassword)

        // THEN
        coVerify(exactly = 1) { mockAuthRepository.updateEmail(any(), any()) }

    }

    @Test
    fun failure() = runTest {
        // GIVEN
        coEvery {
            mockAuthRepository.updateEmail(any(), any())
        } returns Result.Error(mockk())

        // WHEN
        emailUseCase.invoke(email, userPassword)

        // THEN
        coVerify(exactly = 1) { mockAuthRepository.updateEmail(any(), any()) }

    }
}