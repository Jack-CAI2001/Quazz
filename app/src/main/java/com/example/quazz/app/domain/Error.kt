package com.example.quazz.app.domain

import com.example.quazz.R.string.common_error_empty_field
import com.example.quazz.R.string.confirm_password_error_mismatch
import com.example.quazz.R.string.email_error_empty
import com.example.quazz.R.string.email_error_invalid
import com.example.quazz.R.string.firestore_error_aborted
import com.example.quazz.R.string.firestore_error_already_exists
import com.example.quazz.R.string.firestore_error_cancelled
import com.example.quazz.R.string.firestore_error_dataLoss
import com.example.quazz.R.string.firestore_error_deadline_exceed
import com.example.quazz.R.string.firestore_error_failed_precondition
import com.example.quazz.R.string.firestore_error_internal
import com.example.quazz.R.string.firestore_error_invalid_argument
import com.example.quazz.R.string.firestore_error_out_of_range
import com.example.quazz.R.string.firestore_error_permission_denied
import com.example.quazz.R.string.firestore_error_resource_exhausted
import com.example.quazz.R.string.firestore_error_unauthenticated
import com.example.quazz.R.string.firestore_error_unavailable
import com.example.quazz.R.string.firestore_error_unimplemented
import com.example.quazz.R.string.network_error_bad_request
import com.example.quazz.R.string.network_error_forbidden
import com.example.quazz.R.string.network_error_invalid_credentials
import com.example.quazz.R.string.network_error_no_connection
import com.example.quazz.R.string.network_error_not_found
import com.example.quazz.R.string.network_error_server_error
import com.example.quazz.R.string.network_error_timeout
import com.example.quazz.R.string.network_error_unauthorized
import com.example.quazz.R.string.network_error_unknown
import com.example.quazz.R.string.password_error_no_digit
import com.example.quazz.R.string.password_error_no_letter
import com.example.quazz.R.string.password_error_no_uppercase
import com.example.quazz.R.string.password_error_too_short
import com.example.quazz.app.domain.DataError.Network
import com.example.quazz.app.domain.DataError.Network.BAD_REQUEST
import com.example.quazz.app.domain.DataError.Network.FORBIDDEN
import com.example.quazz.app.domain.DataError.Network.INVALID_CREDENTIALS
import com.example.quazz.app.domain.DataError.Network.NETWORK_ERROR
import com.example.quazz.app.domain.DataError.Network.NOT_FOUND
import com.example.quazz.app.domain.DataError.Network.SERVER_ERROR
import com.example.quazz.app.domain.DataError.Network.TIMEOUT
import com.example.quazz.app.domain.DataError.Network.UNAUTHORIZED
import com.example.quazz.app.domain.DataError.Network.UNKNOWN
import com.example.quazz.app.domain.Error.CommonError
import com.example.quazz.app.domain.Error.CommonError.EMPTY_FIELD
import com.example.quazz.app.domain.Error.ConfirmPasswordError
import com.example.quazz.app.domain.Error.ConfirmPasswordError.MISMATCH
import com.example.quazz.app.domain.Error.EmailError
import com.example.quazz.app.domain.Error.EmailError.EMPTY
import com.example.quazz.app.domain.Error.EmailError.INVALID_EMAIL
import com.example.quazz.app.domain.Error.FirebaseFirestoreError
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.ABORTED
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.ALREADY_EXISTS
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.CANCELLED
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.DATA_LOSS
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.DEADLINE_EXCEEDED
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.FAILED_PRECONDITION
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.INTERNAL
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.INVALID_ARGUMENT
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.OK
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.OUT_OF_RANGE
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.PERMISSION_DENIED
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.RESOURCE_EXHAUSTED
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.UNAUTHENTICATED
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.UNAVAILABLE
import com.example.quazz.app.domain.Error.FirebaseFirestoreError.UNIMPLEMENTED
import com.example.quazz.app.domain.Error.PasswordError
import com.example.quazz.app.domain.Error.PasswordError.NO_DIGIT
import com.example.quazz.app.domain.Error.PasswordError.NO_LETTER
import com.example.quazz.app.domain.Error.PasswordError.NO_UPPERCASE
import com.example.quazz.app.domain.Error.PasswordError.TOO_SHORT
import com.example.quazz.app.presentation.UiText
import com.example.quazz.app.presentation.UiText.StringResource
import com.google.firebase.firestore.FirebaseFirestoreException
import retrofit2.HttpException

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
    enum class CommonError: Error {
        EMPTY_FIELD
    }
    enum class FirebaseFirestoreError: Error {
        OK,
        CANCELLED,
        UNKNOWN,
        INVALID_ARGUMENT,
        DEADLINE_EXCEEDED,
        NOT_FOUND,
        ALREADY_EXISTS,
        PERMISSION_DENIED,
        RESOURCE_EXHAUSTED,
        FAILED_PRECONDITION,
        ABORTED,
        OUT_OF_RANGE,
        UNIMPLEMENTED,
        INTERNAL,
        UNAVAILABLE,
        DATA_LOSS,
        UNAUTHENTICATED
    }
}

private fun Error.asUiText(): UiText {
    return when (this) {
        is EmailError -> when(this) {
            EMPTY -> StringResource(email_error_empty)
            INVALID_EMAIL -> StringResource(email_error_invalid)
        }
        is PasswordError -> when(this) {
            TOO_SHORT -> StringResource(password_error_too_short)
            NO_UPPERCASE -> StringResource(password_error_no_uppercase)
            NO_DIGIT -> StringResource(password_error_no_digit)
            NO_LETTER -> StringResource(password_error_no_letter)
        }
        is ConfirmPasswordError -> when(this) {
            MISMATCH -> StringResource(confirm_password_error_mismatch)
        }

        is Network -> when(this) {
            TIMEOUT -> StringResource(network_error_timeout)
            UNKNOWN -> StringResource(network_error_unknown)
            BAD_REQUEST -> StringResource(network_error_bad_request)
            UNAUTHORIZED -> StringResource(network_error_unauthorized)
            FORBIDDEN -> StringResource(network_error_forbidden)
            NOT_FOUND -> StringResource(network_error_not_found)
            SERVER_ERROR -> StringResource(network_error_server_error)
            INVALID_CREDENTIALS -> StringResource(network_error_invalid_credentials)
            NETWORK_ERROR -> StringResource(network_error_no_connection)
        }

        is CommonError -> when(this) {
            EMPTY_FIELD -> StringResource(common_error_empty_field)
        }

        is FirebaseFirestoreError -> when(this) {
            OK -> StringResource(network_error_unknown)
            CANCELLED -> StringResource(firestore_error_cancelled)
            FirebaseFirestoreError.UNKNOWN -> StringResource(network_error_unknown)
            INVALID_ARGUMENT -> StringResource(firestore_error_invalid_argument)
            DEADLINE_EXCEEDED -> StringResource(firestore_error_deadline_exceed)
            FirebaseFirestoreError.NOT_FOUND -> StringResource(network_error_not_found)
            ALREADY_EXISTS -> StringResource(firestore_error_already_exists)
            PERMISSION_DENIED -> StringResource(firestore_error_permission_denied)
            RESOURCE_EXHAUSTED -> StringResource(firestore_error_resource_exhausted)
            FAILED_PRECONDITION -> StringResource(firestore_error_failed_precondition)
            ABORTED -> StringResource(firestore_error_aborted)
            OUT_OF_RANGE -> StringResource(firestore_error_out_of_range)
            UNIMPLEMENTED -> StringResource(firestore_error_unimplemented)
            INTERNAL -> StringResource(firestore_error_internal)
            UNAVAILABLE -> StringResource(firestore_error_unavailable)
            DATA_LOSS -> StringResource(firestore_error_dataLoss)
            UNAUTHENTICATED -> StringResource(firestore_error_unauthenticated)
        }
    }
}

fun Result.Error<*, Error>.asErrorUiText(): UiText {
    return error.asUiText()
}

fun handleFirebaseException(e: FirebaseFirestoreException): Result<Nothing, Error> {
    return when (e.code) {
        FirebaseFirestoreException.Code.OK -> Result.Error(OK)
        FirebaseFirestoreException.Code.CANCELLED -> Result.Error(CANCELLED)
        FirebaseFirestoreException.Code.UNKNOWN -> Result.Error(FirebaseFirestoreError.UNKNOWN)
        FirebaseFirestoreException.Code.INVALID_ARGUMENT -> Result.Error(INVALID_ARGUMENT)
        FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> Result.Error(DEADLINE_EXCEEDED)
        FirebaseFirestoreException.Code.NOT_FOUND -> Result.Error(FirebaseFirestoreError.NOT_FOUND)
        FirebaseFirestoreException.Code.ALREADY_EXISTS -> Result.Error(ALREADY_EXISTS)
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> Result.Error(PERMISSION_DENIED)
        FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> Result.Error(RESOURCE_EXHAUSTED)
        FirebaseFirestoreException.Code.FAILED_PRECONDITION -> Result.Error(FAILED_PRECONDITION)
        FirebaseFirestoreException.Code.ABORTED -> Result.Error(ABORTED)
        FirebaseFirestoreException.Code.OUT_OF_RANGE -> Result.Error(OUT_OF_RANGE)
        FirebaseFirestoreException.Code.UNIMPLEMENTED -> Result.Error(UNIMPLEMENTED)
        FirebaseFirestoreException.Code.INTERNAL -> Result.Error(INTERNAL)
        FirebaseFirestoreException.Code.UNAVAILABLE -> Result.Error(UNAVAILABLE)
        FirebaseFirestoreException.Code.DATA_LOSS -> Result.Error(DATA_LOSS)
        FirebaseFirestoreException.Code.UNAUTHENTICATED -> Result.Error(UNAUTHENTICATED)
    }
}

fun handleHttpException(e: HttpException): Result<Nothing, Error> {
    return when (e.code()) {
        400 -> Result.Error(BAD_REQUEST)
        408 -> Result.Error(TIMEOUT)
        401 -> Result.Error(UNAUTHORIZED)
        403 -> Result.Error(FORBIDDEN)
        404 -> Result.Error(NOT_FOUND)
        500 -> Result.Error(SERVER_ERROR)
        else -> Result.Error(UNKNOWN)
    }
}

fun handleError(e: Exception): Result<Nothing, Error> {
    return when (e) {
        is HttpException -> handleHttpException(e)
        is FirebaseFirestoreException -> handleFirebaseException(e)
        else -> Result.Error(UNKNOWN)
    }
}