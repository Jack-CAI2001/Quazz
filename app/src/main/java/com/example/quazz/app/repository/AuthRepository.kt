package com.example.quazz.app.repository

import com.example.quazz.app.source.network.service.AccountService
import javax.inject.Inject

class AuthRepository @Inject constructor (
    private val accountService: AccountService
) {
    suspend fun signUp(email: String, password: String) = accountService.signUp(email, password)
    suspend fun signIn(email: String, password: String) = accountService.signIn(email, password)
    fun hasUser() = accountService.hasUser()
}