package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import javax.inject.Inject
typealias ConfirmPasswordError = Error.ConfirmPasswordError
typealias ConfirmPasswordResult = Result<Unit, ConfirmPasswordError>
class ConfirmPasswordValidator @Inject constructor() {
    fun execute(password: String, confirmPassword: String): ConfirmPasswordResult {
        if (password != confirmPassword) {
            return Result.Error(Error.ConfirmPasswordError.MISMATCH)
        }
        return Result.Success(Unit)
    }
}