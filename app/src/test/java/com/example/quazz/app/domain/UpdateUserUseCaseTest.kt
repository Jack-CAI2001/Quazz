package com.example.quazz.app.domain

import com.example.quazz.app.model.User
import com.example.quazz.app.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class UpdateUserUseCaseTest {
    private val mockUserRepository = mockk<UserRepository>(relaxed = true)
    private val userCase = UpdateUserUseCase(mockUserRepository)
    private val user = mockk<User>()

    @Test
    fun success() = runTest{
        // GIVEN
        coEvery {
            mockUserRepository.updateUser(any())
        } returns Result.Success(Unit)
        // WHEN
        userCase.invoke(user)
        //THEN
        coVerify (exactly = 1) { mockUserRepository.updateUser(any()) }
    }

    @Test
    fun failure() = runTest{
        // GIVEN
        coEvery {
            mockUserRepository.updateUser(any())
        } returns Result.Error(mockk())
        // WHEN
        userCase.invoke(user)
        //THEN
        coVerify (exactly = 1) { mockUserRepository.updateUser(any()) }
    }
}