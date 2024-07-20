package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.UserService
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserServiceImpl @Inject constructor() : UserService {
    override suspend fun getUser(uid: String): Result<User, DataError.Network> {
        return try {
            val user = Firebase.firestore
                .collection(USER)
                .document(uid)
                .get(Source.SERVER)
                .await()
                .toObject(User::class.java)
            Result.Success(user!!)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit, DataError.Network> {
        return try {
            Firebase.firestore
                .collection(USER)
                .document(user.uid)
                .set(user)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun updateUserEmail(email: String): Result<Unit, DataError.Network> {
        return try {
            val user = Firebase.auth.currentUser!!
            Firebase.firestore
                .collection(USER)
                .document(user.uid)
                .update("email", email)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    companion object {
        private const val USER = "user"
    }
}