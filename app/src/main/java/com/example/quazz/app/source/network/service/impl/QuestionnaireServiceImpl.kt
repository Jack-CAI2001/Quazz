package com.example.quazz.app.source.network.service.impl

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import com.example.quazz.app.domain.handleError
import com.example.quazz.app.model.Questionnaire
import com.example.quazz.app.model.Quizz
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.AUTHOR
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.DESCRIPTION
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.QUESTION_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.QUIZ_COLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.Quiz.TITLE
import com.example.quazz.app.source.network.service.DatabaseConstants.User.QUIZZ_CREATED_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.User.QUIZZ_LIST_SUBCOLLECTION
import com.example.quazz.app.source.network.service.DatabaseConstants.User.USER_COLLECTION
import com.example.quazz.app.source.network.service.QuestionnaireService
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionnaireServiceImpl @Inject constructor(): QuestionnaireService {
    override suspend fun createQuestionnaire(
        quizz: Quizz
    ): Result<Unit, Error> {
        return try {
            // Save the quizz to the database
            val quizRef = Firebase.firestore
                .collection(QUIZ_COLLECTION)
                .document()
            val subCollectionQuestionRef = quizRef
                .collection(QUESTION_SUBCOLLECTION)

            val userRef = Firebase.firestore.collection(USER_COLLECTION).document(quizz.user.uid)
            val subCollectionQuizCreatedRef = userRef.collection(QUIZZ_CREATED_SUBCOLLECTION).document()
            val subCollectionQuizList = userRef.collection(QUIZZ_LIST_SUBCOLLECTION).document()
            val subCollectionQuestionQuizList = subCollectionQuizList.collection(
                QUESTION_SUBCOLLECTION
            )

            Firebase.firestore.runTransaction { transaction ->
                transaction.set(quizRef, mapOf(
                    AUTHOR to userRef,
                    TITLE to quizz.title,
                    DESCRIPTION to quizz.description
                ))
                quizz.questionnaires.forEach { questionnaire ->
                    transaction.set(subCollectionQuestionRef.document(), questionnaire) // quizz ajoute chaque question
                    transaction.set(subCollectionQuestionQuizList.document(), questionnaire)
                }

                transaction.set(subCollectionQuizList, mapOf(
                    AUTHOR to userRef,
                    TITLE to quizz.title,
                    DESCRIPTION to quizz.description
                )) // attribue user une copie du quizz dans quizzList

                transaction.set(subCollectionQuizCreatedRef, mapOf(
                    "quizz" to quizRef
                )) // attribue user au ref quizz
            }.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun getQuizzById(quizzId: String): Result<Quizz, Error> {
        return try {
            val quizz = Firebase.firestore
                .collection(QUIZ_COLLECTION)
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
}