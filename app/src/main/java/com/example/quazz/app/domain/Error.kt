package com.example.quazz.app.domain

import com.example.quazz.R
import com.example.quazz.app.presentation.UiText

sealed interface Error {
    enum class PasswordError: Error {
        TOO_SHORT,
        NO_UPPERCASE,
        NO_DIGIT,
        NO_LETTER
    }
    enum class ConfirmPasswordError: Error {
        MISMATCH
    }
    enum class EmailError: Error {
        EMPTY,
        INVALID_EMAIL
    }
}

private fun Error.asUiText(): UiText {
    return when (this) {
        is Error.EmailError -> when(this) {
            Error.EmailError.EMPTY -> UiText.StringResource(R.string.email_error_empty)
            Error.EmailError.INVALID_EMAIL -> UiText.StringResource(R.string.email_error_invalid)
        }
        is Error.PasswordError -> when(this) {
            Error.PasswordError.TOO_SHORT -> UiText.StringResource(R.string.password_error_too_short)
            Error.PasswordError.NO_UPPERCASE -> UiText.StringResource(R.string.password_error_no_uppercase)
            Error.PasswordError.NO_DIGIT -> UiText.StringResource(R.string.password_error_no_digit)
            Error.PasswordError.NO_LETTER -> UiText.StringResource(R.string.password_error_no_letter)
        }
        is Error.ConfirmPasswordError -> when(this) {
            Error.ConfirmPasswordError.MISMATCH -> UiText.StringResource(R.string.confirm_password_error_mismatch)
        }
        DataError.Network.TIMEOUT -> UiText.StringResource(R.string.network_error_timeout)
        DataError.Network.UNKNOWN -> UiText.StringResource(R.string.network_error_unknown)
        DataError.Network.BAD_REQUEST -> UiText.StringResource(R.string.network_error_bad_request)
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.network_error_unauthorized)
        DataError.Network.FORBIDDEN -> UiText.StringResource(R.string.network_error_forbidden)
        DataError.Network.NOT_FOUND -> UiText.StringResource(R.string.network_error_not_found)
        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.network_error_server_error)
    }
}

fun Result.Error<*, Error>.asErrorUiText(): UiText {
    return error.asUiText()
}