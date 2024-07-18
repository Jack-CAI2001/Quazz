package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import javax.inject.Inject

typealias CommonError = Error.CommonError
class PseudoValidator @Inject constructor(){
    fun execute(email: String): Result<Unit, CommonError> {
        if (email.isBlank()) {
            return Result.Error(Error.CommonError.EMPTY_FIELD)
        }
        return Result.Success(Unit)
    }
}