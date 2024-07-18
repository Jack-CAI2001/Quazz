package com.example.quazz.app.repository

import com.example.quazz.app.source.network.service.AccountService
import javax.inject.Inject

class AuthRepository @Inject constructor (
    private val accountService: AccountService
) {
    suspend fun signUp(pseudo: String, email: String, password: String) = accountService.signUp(pseudo, email, password)
    suspend fun signIn(email: String, password: String) = accountService.signIn(email, password)
    fun hasUser() = accountService.hasUser()
    val currentUserUid = accountService.currentUserId
    suspend fun getUser() = accountService.getUser()
    fun logOut() = accountService.signOut()
}