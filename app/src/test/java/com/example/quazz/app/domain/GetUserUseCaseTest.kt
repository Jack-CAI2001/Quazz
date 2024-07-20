package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import com.example.quazz.app.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetUserUseCaseTest {
    private val mockAuthRepository = mockk<AuthRepository>(relaxed = true)
    private val mockUserRepository = mockk<UserRepository>(relaxed = true)
    private val userCase = GetUserUseCase(mockUserRepository, mockAuthRepository)
    @Test
    fun success() = runTest {
        // GIVEN
        coEvery {
            mockAuthRepository.getUser()
            mockUserRepository.getUser(any())
        } returns Result.Success(mockk())
        // WHEN
        userCase.invoke()
        // THEN
        coVerify(exactly = 1) {
            mockAuthRepository.getUser()
            mockUserRepository.getUser(any())
        }
    }
    @Test
    fun failureWithUser() = runTest {
        // GIVEN
        coEvery {
            mockAuthRepository.currentUserUid
        } returns "uid"
        coEvery {
            mockUserRepository.getUser(any())
        } returns Result.Error(mockk())
        // WHEN
        userCase.invoke()
        // THEN
        coVerify {
            mockAuthRepository.currentUserUid
            mockUserRepository.getUser(any())

        }
        coVerify(atLeast = 0) {
            mockAuthRepository.getUser()
        }
        assertEquals(userCase.invoke() is Result.Error, true)
    }

    @Test
    fun failureWithAuth() = runTest {
        // GIVEN
        coEvery {
            mockAuthRepository.currentUserUid
        } returns "uid"
        coEvery {
            mockUserRepository.getUser(any())
        } returns Result.Success(mockk())
        coEvery {
            mockAuthRepository.getUser()
        } returns Result.Error(mockk())
        // WHEN
        userCase.invoke()
        // THEN
        coVerify {
            mockAuthRepository.currentUserUid
            mockUserRepository.getUser(any())
            mockAuthRepository.getUser()
        }
        assertEquals(userCase.invoke() is Result.Error, true)
    }
}