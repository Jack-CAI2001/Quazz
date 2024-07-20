package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import javax.inject.Inject

class PasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newPassword: String, oldPassword: String): Result<Unit, DataError.Network> {
        return authRepository.updatePassword(newPassword, oldPassword)
    }
}