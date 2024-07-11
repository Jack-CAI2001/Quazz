package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EmailValidatorTest {

    private val useCase = EmailValidator()
    @Test
    fun success() {
        //GIVEN
        val email = "email@example.com"
        //WHEN
        useCase.execute(email)
        //THEN
        val result = useCase.execute(email)
        Assertions.assertEquals((result as Result.Success).data, Unit)
    }

    @Test
    @DisplayName("GIVEN invalid_email THEN error")
    fun invalid_email_error() {
        //GIVEN
        val email = "email"
        //WHEN
        useCase.execute(email)
        //THEN
        val result = useCase.execute(email)
        Assertions.assertEquals((result as Result.Error).error, Error.EmailError.INVALID_EMAIL)
    }

    @Test
    @DisplayName("GIVEN blank mail THEN error")
    fun blank_email_error() {
        //GIVEN
        val email = ""
        //WHEN
        useCase.execute(email)
        //THEN
        val result = useCase.execute(email)
        Assertions.assertEquals((result as Result.Error).error, Error.EmailError.EMPTY)
    }
}