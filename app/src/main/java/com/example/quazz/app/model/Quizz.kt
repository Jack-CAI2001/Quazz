package com.example.quazz.app.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Quizz(
    val id: String = "",
    val user: User = User(),
    val title: String = "",
    val description: String = "",
    val questionnaires: List<Questionnaire> = emptyList()
)

object CustomNavType {

    val QuizzType = object : NavType<Quizz>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): Quizz? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Quizz {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Quizz): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Quizz) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}