package com.example.quazz.app.domain

import com.example.quazz.app.model.User
import com.example.quazz.app.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit, Error> {
        return userRepository.updateUser(user)
    }
}