package com.example.quazz.app.source.network.service

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.google.firebase.auth.FirebaseUser

interface AccountService {
    val currentUserId: String

    suspend fun getUser(): Result<FirebaseUser, DataError.Network>
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Result<Unit, Error>
    suspend fun signUp(pseudo: String, email: String, password: String): Result<Unit, Error>
    fun signOut()
    suspend fun deleteAccount()
    suspend fun updatePassword(newPassword: String, oldPassword: String): Result<Unit, Error>
    suspend fun updateEmail(newEmail: String, password: String): Result<Unit, Error>
}