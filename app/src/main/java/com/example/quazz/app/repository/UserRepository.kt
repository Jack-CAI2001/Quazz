package com.example.quazz.app.repository

import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
){
    suspend fun getUser(uid: String) = userService.getUser(uid)

    suspend fun updateUser(user: User) = userService.updateUser(user)

    suspend fun updateUserEmail(email: String) = userService.updateUserEmail(email)

}