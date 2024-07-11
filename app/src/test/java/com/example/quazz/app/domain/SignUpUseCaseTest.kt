package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SignUpUseCaseTest {

    private val mockAuthRepository = mockk<AuthRepository>(relaxed = true)
    private val userCase = SignUpUseCase(mockAuthRepository)
    private val email = ""
    private val password = ""
    @Test
    fun success() = runTest{
        // GIVEN
        coEvery {
            mockAuthRepository.signUp(any(), any())
        } returns Result.Success(Unit)
        // WHEN
        userCase.invoke(email, password)
        //THEN
        coVerify (exactly = 1) { mockAuthRepository.signUp(any(), any()) }
    }

    @Test
    fun failure() = runTest{
        // GIVEN
        coEvery {
            mockAuthRepository.signUp(any(), any())
        } returns Result.Error(mockk())
        // WHEN
        userCase.invoke(email, password)
        //THEN
        coVerify (exactly = 1) { mockAuthRepository.signUp(any(), any()) }
    }
}