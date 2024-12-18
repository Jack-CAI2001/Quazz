package com.example.quazz.app.source.network.service

object DatabaseConstants {
    object Quiz {
        const val QUIZ_COLLECTION = "quiz"
        const val QUESTION_SUBCOLLECTION = "question"
        const val AUTHOR = "author"
        const val DESCRIPTION = "description"
        const val TITLE = "title"
    }
    object User {
        const val USER_COLLECTION = "user"
        const val QUIZZ_CREATED_SUBCOLLECTION = "quizzCreated"
        const val QUIZZ_LIST_SUBCOLLECTION = "quizzList"
        const val UID = "uid"
        const val EMAIL = "email"
        const val PSEUDO ="pseudo"
    }
}