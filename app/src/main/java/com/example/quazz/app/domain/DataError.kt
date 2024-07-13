package com.example.quazz.app.domain

sealed interface DataError: Error {
    enum class Network : DataError {
        TIMEOUT,
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        SERVER_ERROR,
        INVALID_CREDENTIALS,
        NETWORK_ERROR,
        UNKNOWN
    }
}