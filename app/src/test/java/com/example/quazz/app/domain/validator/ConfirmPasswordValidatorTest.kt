package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ConfirmPasswordValidatorTest {
    private val useCase = ConfirmPasswordValidator()

    @Test
    fun success() {
        //GIVEN
        val password = "password"
        val confirmPassword = "password"
        //WHEN
        useCase.execute(password, confirmPassword)
        //THEN
        val result = useCase.execute(password, confirmPassword)
        Assertions.assertEquals((result as Result.Success).data, Unit)
    }

    @Test
    fun error() {
        //GIVEN
        val password = "password"
        val confirmPassword = ""
        //WHEN
        useCase.execute(password, confirmPassword)
        //THEN
        val result = useCase.execute(password, confirmPassword)
        Assertions.assertEquals((result as Result.Error).error, Error.ConfirmPasswordError.MISMATCH)
    }
}