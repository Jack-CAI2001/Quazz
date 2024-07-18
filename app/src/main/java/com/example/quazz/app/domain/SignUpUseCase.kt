package com.example.quazz.app.domain

import com.example.quazz.app.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(pseudo: String, email: String, password: String): Result<Unit, Error> {
        return authRepository.signUp(pseudo.trim(), email.trim(), password)
    }
}