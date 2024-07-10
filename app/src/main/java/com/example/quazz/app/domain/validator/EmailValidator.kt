package com.example.quazz.app.domain.validator

import android.util.Patterns
import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import javax.inject.Inject
typealias EmailError = Error.EmailError
typealias EmailResult = Result<Unit, EmailError>
class EmailValidator @Inject constructor(){
    fun execute(email: String): EmailResult {
        if (email.isBlank()) {
            return Result.Error(Error.EmailError.EMPTY)
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error(Error.EmailError.INVALID_EMAIL)
        }

        return Result.Success(Unit)
    }
}