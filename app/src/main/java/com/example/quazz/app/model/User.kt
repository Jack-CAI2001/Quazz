package com.example.quazz.app.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val pseudo: String = "",
)