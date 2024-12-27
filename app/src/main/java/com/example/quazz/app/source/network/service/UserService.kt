package com.example.quazz.app.source.network.service

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.model.User


interface UserService {
    suspend fun getUser(uid: String): Result<User, Error>
    suspend fun updateUser(user: User): Result<Unit, Error>
    suspend fun updateUserEmail(email: String): Result<Unit, Error>
    suspend fun getQuizzCreated(uid: String): Result<List<Quizz>, Error>
    suspend fun getQuizzList(uid: String): Result<List<Quizz>, Error>
}