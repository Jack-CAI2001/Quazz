package com.example.quazz.app.source.network.service

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.User

interface UserService {
    suspend fun getUser(uid: String): Result<User, DataError.Network>
    suspend fun updateUser(user: User): Result<Unit, DataError.Network>
    suspend fun updateUserEmail(email: String): Result<Unit, DataError.Network>
}