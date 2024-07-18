package com.example.quazz.app.domain.validator

import com.example.quazz.app.domain.Error
import com.example.quazz.app.domain.Result
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PseudoValidatorTest {
    private val useCase = PseudoValidator()
    @Test
    fun success() {
        //GIVEN
        val pseudo = "pseudo"
        //WHEN
        useCase.execute(pseudo)
        //THEN
        val result = useCase.execute(pseudo)
        Assertions.assertEquals((result as Result.Success).data, Unit)
    }

    @Test
    fun error() {
        //GIVEN
        val pseudo = "    "
        //WHEN
        useCase.execute(pseudo)
        //THEN
        val result = useCase.execute(pseudo)
        Assertions.assertEquals((result as Result.Error).error, Error.CommonError.EMPTY_FIELD)
    }
}