package com.example.quazz.app.domain

import com.example.quazz.app.model.User
import com.example.quazz.app.repository.AuthRepository
import com.example.quazz.app.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User, DataError.Network>{
        val resultUser =  userRepository.getUser(authRepository.currentUserUid)

        return when(resultUser){
            is Result.Success -> {
                when(val resultFireBaseUser = authRepository.getUser()){
                    is Result.Success -> {
                        val user = resultUser.data.copy(
                            uid = resultFireBaseUser.data.uid,
                            email = resultFireBaseUser.data.email?: ""
                        )
                        Result.Success(user)
                    }

                    is Result.Error -> Result.Error(resultFireBaseUser.error)
                }
            }

            is Result.Error -> Result.Error(resultUser.error)
        }
    }
}