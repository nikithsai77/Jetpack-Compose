package com.android.compose.mockk.part1

import org.junit.Assert
import org.junit.Test

class EmailValidatorsTest {

    @Test
    fun whenEmailIsValid_shouldReturnTrue() {
        val email = "test@gmail.com"
        val result = EmailValidators.isValidEmail(email = email)
        Assert.assertTrue(result)
    }

    @Test
    fun whenEmailIsInValid_shouldReturnFalse() {
        val email = "test.com"
        val result = EmailValidators.isValidEmail(email = email)
        Assert.assertFalse(result)
    }

}
