package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.DataError
import com.example.quazz.app.domain.Result
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.User
import com.example.quazz.app.source.network.service.DatabaseConstants.QUESTION_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.QUIZZ_CREATED_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.QUIZZ_LIST_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.QUIZ_COLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.USER_COLLECTION
import com.example.quazz.app.source.network.service.QuestionnaireService
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionnaireServiceImpl @Inject constructor(): QuestionnaireService {
    override suspend fun createQuestionnaire(
        user: User,
        title: String,
        description: String,
        questionnaire: List<Questionnaire>
    ): Result<Unit, DataError.Network> {
        return try {
            // Save the quizz to the database
            val quizRef = Firebase.firestore
                .collection(QUIZ_COLLECTION)
                .document()
            val subCollectionQuestionRef = quizRef
                .collection(QUESTION_SUBCOLLECTION)

            val userRef = Firebase.firestore.collection(USER_COLLECTION).document(user.uid)
            val subCollectionQuizCreatedRef = userRef.collection(QUIZZ_CREATED_SUBCOLLECTION).document()
            val subCollectionQuizList = userRef.collection(QUIZZ_LIST_SUBCOLLECTION).document()
            val subCollectionQuestionQuizList = subCollectionQuizList.collection(QUESTION_SUBCOLLECTION)
            Firebase.firestore.runTransaction { transaction ->
                transaction.set(quizRef, mapOf(
                    "author" to userRef,
                    "title" to title,
                    "description" to description
                ))
                questionnaire.forEach { questionnaire ->
                    transaction.set(subCollectionQuestionRef.document(), questionnaire)
                } // quizz ajoute chaque question

                transaction.set(subCollectionQuizList, mapOf(
                    "author" to userRef,
                    "title" to title,
                    "description" to description
                )) // attribue user à la copie de quizz
                questionnaire.forEach { questionnaire ->
                    transaction.set(subCollectionQuestionQuizList.document(), questionnaire)
                } // quizz ajoute chaque question

                transaction.set(subCollectionQuizCreatedRef, mapOf(
                    "quizz" to quizRef
                )) // attribue user au ref quizz


            }.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}