package com.example.quazz.app.source.network.service

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String

    suspend fun getUser(): Result<FirebaseUser, DataError.Network>
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Result<Unit, DataError.Network>
    suspend fun signUp(pseudo: String, email: String, password: String): Result<Unit, DataError.Network>
    fun signOut()
    suspend fun deleteAccount()
}