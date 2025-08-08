package com.android.compose.basic

object RegistrationUtil {

    private val existingUsers = listOf("Imhotep", "Anacksunamun")

    fun validateRegistrationInput(userName: String, passWord: String, confirmPassword: String) : Boolean {
        return if (userName.trim().isEmpty() || passWord.trim().isEmpty() || confirmPassword.trim().isEmpty()) false
        else if (passWord.trim() != confirmPassword.trim()) false
        else if (existingUsers.contains(userName.trim())) false
        else if (passWord.count { it.isDigit() } < 2 ) false
        else if (passWord.count { it.isLetter() } == 0 ) false
        else true
    }

    /* n = 6, return value must be 8
    1  2  3  4  5  6  7
    0, 1, 1, 2, 3, 5, 8 */
    fun fib(n : Int) : Int {
        val list = mutableListOf(0, 1)
        for (i in 1..n - 1) {
            val result = list[i] + list[i - 1]
            list.add(result)
        }
        return list[list.lastIndex]
    }

    fun fibNano(n: Int) : Int {
        var a = 0
        var b = 1
        var c = 0
//      0, 1, 1,
//      a  b  c  2
//            a  b  3  5
//               a  b  c
        repeat((1..n-1).count()) {
            c = a + b
            a = b
            b = c
        }
        return c
    }

}