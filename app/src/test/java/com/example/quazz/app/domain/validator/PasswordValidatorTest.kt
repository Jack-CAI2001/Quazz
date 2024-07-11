package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Result
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class PasswordValidatorTest {
    private val useCase = PasswordValidator()
    @Test
    fun success() {
        // GIVEN
        val password = "passwordAz1"
        // WHEN
        useCase.execute(password)
        // THEN
        val result = useCase.execute(password)
        Assertions.assertEquals((result as Result.Success).data, Unit)
    }

    @Nested
    @DisplayName("password fail")
    inner class PasswordFail {
        @Test
        @DisplayName("Description here")
        fun length_error() {
            // GIVEN
            val password = "passw"
            // WHEN
            useCase.execute(password)
            // THEN
            val result = useCase.execute(password)
            Assertions.assertEquals((result as Result.Error).error, PasswordError.TOO_SHORT)
        }
        @Test
        @DisplayName("Description here")
        fun uppercase_error() {
            // GIVEN
            val password = "password"
            // WHEN
            useCase.execute(password)
            // THEN
            val result = useCase.execute(password)
            Assertions.assertEquals((result as Result.Error).error, PasswordError.NO_UPPERCASE)
        }
        @Test
        @DisplayName("Description here")
        fun digit_error() {
            // GIVEN
            val password = "passwordAz"
            // WHEN
            useCase.execute(password)
            // THEN
            val result = useCase.execute(password)
            Assertions.assertEquals((result as Result.Error).error, PasswordError.NO_DIGIT)
        }
        @Test
        @DisplayName("Description here")
        fun letter_error() {
            // GIVEN
            val password = "123456"
            // WHEN
            useCase.execute(password)
            // THEN
            val result = useCase.execute(password)
            Assertions.assertEquals((result as Result.Error).error, PasswordError.NO_LETTER)
        }
    }
}