package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import javax.inject.Inject

class UserConnectedUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Boolean =
        authRepository.hasUser()
}