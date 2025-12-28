package com.android.compose.mockk.part1

object EmailValidators {

    fun isValidEmail(email: String) : Boolean {
        return email.contains(other = "@") && email.contains(other = ".")
    }

}
