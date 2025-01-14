package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.handleError
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.AUTHOR
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.DESCRIPTION
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.QUESTION_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.QUIZ_COLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.TITLE
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
import com.google.firebase.firestore.toObject
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

    override suspend fun getUserQuizzById(quizzId: String, uid: String): Result<Quizz, Error> {
        return try {
            val quizz = Firebase.firestore
                .collection(USER_COLLECTION)
                .document(uid)
                .collection(QUIZZ_LIST_SUBCOLLECTION)
                .document(quizzId)

            val quizzResponse = quizz.get(Source.SERVER).await().toObject<Quizz>()

            val questionsList = mutableListOf<Questionnaire>()

            val questions = quizz
                .collection(QUESTION_SUBCOLLECTION)
                .get(Source.SERVER)
                .await()

            questions.mapNotNull {
                question ->
                if (question.data["options"]!=null) {
                    questionsList.add(question.toObject(Questionnaire.ChoiceQuestion::class.java))
                } else {
                    questionsList.add(question.toObject(Questionnaire.TextEntryQuestion::class.java))
                }

            }

            if (quizzResponse == null) {
                throw Exception("Quizz data not found or invalid")
            }

            Result.Success(quizzResponse.copy(id = quizzId, questionnaires = questionsList))
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun addQuizzToQuizzList(quizz: Quizz, uid: String): Result<Unit, Error> {
        return try {

            val quizzAuthorRef = Firebase.firestore.collection(USER_COLLECTION).document(quizz.user.uid)

            val userRef = Firebase.firestore.collection(USER_COLLECTION).document(uid)
            val subCollectionQuizList = userRef.collection(QUIZZ_LIST_SUBCOLLECTION).document()
            val subCollectionQuestionQuizList = subCollectionQuizList.collection(
                QUESTION_SUBCOLLECTION
            )

            Firebase.firestore.runTransaction { transaction ->

                quizz.questionnaires.forEach { questionnaire ->
                    transaction.set(subCollectionQuestionQuizList.document(), questionnaire)
                }

                transaction.set(subCollectionQuizList, mapOf(
                    AUTHOR to quizzAuthorRef,
                    TITLE to quizz.title,
                    DESCRIPTION to quizz.description
                )) // attribue user une copie du quizz dans quizzList

            }
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }
}