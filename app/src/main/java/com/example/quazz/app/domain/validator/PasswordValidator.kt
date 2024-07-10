package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import javax.inject.Inject

typealias PasswordError = Error.PasswordError
typealias PasswordResult = Result<Unit, PasswordError>
class PasswordValidator @Inject constructor() {
    fun execute(password: String): PasswordResult {
        if (password.length < 6) {
            return Result.Error(Error.PasswordError.TOO_SHORT)
        }
        val hasUppercase = password.any { it.isUpperCase() }
        if (!hasUppercase) {
            return Result.Error(Error.PasswordError.NO_UPPERCASE)
        }
        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) {
            return Result.Error(Error.PasswordError.NO_DIGIT)
        }
        val hasLetter = password.any { it.isLetter() }
        if (!hasLetter) {
            return Result.Error(Error.PasswordError.NO_LETTER)
        }
        return Result.Success(Unit)
    }
}