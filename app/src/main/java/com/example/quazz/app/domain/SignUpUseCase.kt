package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(email: String, password: String): Result<Unit, Error> {
        return authRepository.signUp(email.trim(), password)
    }
}