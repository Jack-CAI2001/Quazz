package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.handleError
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.DatabaseConstants.User.EMAIL
import com.example.quazz.app.source.network.service.DatabaseConstants.User.USER_COLLECTION
import com.example.quazz.app.source.network.service.UserService
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserServiceImpl @Inject constructor() : UserService {
    override suspend fun getUser(uid: String): Result<User, Error> {
        return try {
            val user = Firebase.firestore
                .collection(USER_COLLECTION)
                .document(uid)
                .get(Source.SERVER)
                .await()
                .toObject(User::class.java)
            Result.Success(user!!)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit, Error> {
        return try {
            Firebase.firestore
                .collection(USER_COLLECTION)
                .document(user.uid)
                .set(user)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun updateUserEmail(email: String): Result<Unit, Error> {
        return try {
            val user = Firebase.auth.currentUser!!
            Firebase.firestore
                .collection(USER_COLLECTION)
                .document(user.uid)
                .update(EMAIL, email)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }
}