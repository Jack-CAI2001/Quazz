package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import javax.inject.Inject

class EmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit, DataError.Network> {
        return authRepository.updateEmail(email.trim(), password)
    }
}
