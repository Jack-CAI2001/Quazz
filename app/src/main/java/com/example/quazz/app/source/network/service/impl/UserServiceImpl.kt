package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.handleError
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.QUIZ_COLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.User.EMAIL
import com.example.quazz.app.source.network.service.DatabaseConstants.User.QUIZZ_CREATED_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.User.QUIZZ_LIST_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.User.USER_COLLECTION
import com.example.quazz.app.source.network.service.UserService
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
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

    override suspend fun getQuizzCreated(uid: String): Result<List<Quizz>, Error> {
        return try {
            val userQuizzCreatedList = mutableListOf<Quizz>()
            val quizzCreatedList = Firebase.firestore
                .collection(USER_COLLECTION)
                .document(uid)
                .collection(QUIZZ_CREATED_SUBCOLLECTION)
                .get(Source.SERVER)
                .await()

            quizzCreatedList.mapNotNull {
                quizzCreated ->
                quizzCreated.data["quizz"].let {
                    reference ->
                    val ref = (reference as DocumentReference)
                    val quizz = Firebase.firestore.collection(QUIZ_COLLECTION).document(ref.id).get().await()
                    userQuizzCreatedList.add(Quizz(id = ref.id, title = quizz.data?.get("title").toString(), description = quizz.data?.get("description").toString()))
                }
            }
            Result.Success(userQuizzCreatedList.toList())
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun getQuizzList(uid: String): Result<List<Quizz>, Error> {
        return try {
            val userCreatedList = mutableListOf<Quizz>()
            val quizzList = Firebase.firestore
                .collection(USER_COLLECTION)
                .document(uid)
                .collection(QUIZZ_LIST_SUBCOLLECTION)
                .get(Source.SERVER)
                .await()
            quizzList.mapNotNull { quizz ->
                userCreatedList.add(Quizz(id = quizz.id, title = quizz.data["title"].toString(), description = quizz.data.get("description")
                    .toString()))
            }

            Result.Success(userCreatedList.toList())
        } catch (e: Exception) {
            handleError(e)
        }
    }
}