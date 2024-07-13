package com.example.quazz.app.source.network.service

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Result<Unit, DataError.Network>
    suspend fun signUp(email: String, password: String): Result<Unit, DataError.Network>
    suspend fun signOut()
    suspend fun deleteAccount()
}