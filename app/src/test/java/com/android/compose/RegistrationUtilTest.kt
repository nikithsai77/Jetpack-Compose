package com.android.compose

import com.android.compose.basic.RegistrationUtil
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.Assert

class RegistrationUtilTest {

    @Test
    fun `empty username returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(userName = "", passWord = "123", confirmPassword = "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `existing user name returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(userName = "Imhotep", passWord = "123", confirmPassword = "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `password and confirm password are incorrect then returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(userName = "Imhotep", passWord = "1234", confirmPassword = "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(userName = "Imhotep", passWord = "", confirmPassword = "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and correctly password returns true`() {
        val result = RegistrationUtil.validateRegistrationInput(userName = "O'Connell", passWord = "a123", confirmPassword = "a123")
        assertThat(result).isTrue()
    }

    @Test
    fun `test fib series number`() {
        val result = RegistrationUtil.fib(n = 6)
        Assert.assertEquals(8, result)
        val result1 = RegistrationUtil.fib(n = 7)
        Assert.assertEquals(13, result1)
    }

    @Test
    fun `test fibNano series number`() {
        val result = RegistrationUtil.fibNano(n = 6)
        Assert.assertEquals(8, result)
    }

}