package com.darkwhite.feature.createaccount

import android.util.Patterns
import androidx.core.text.isDigitsOnly

object Util {
    
    fun String.isValidUsername(): Boolean {
        val isLengthValid = this.length >= 8
        val isAlphanumeric = this.all { it.isLetterOrDigit() }
        return isLengthValid && isAlphanumeric
        // Or regex
//        val regex = Regex("^[a-zA-Z0-9]{8,}$")
//        return regex.matches(this)
    }
    
    fun String.isValidEmail(): Boolean {
        return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
    
    fun String.isValidReference(): Boolean {
        return this.isDigitsOnly()
    }
    
    fun String.isValidPassword(): Boolean {
        val minLength = 8
        val hasUpperCase = this.any { it.isUpperCase() }
        val hasLowerCase = this.any { it.isLowerCase() }
        val hasDigit = this.any { it.isDigit() }
        val hasSpecialChar = this.any { !it.isLetterOrDigit() }
        val hasNoSpace = !this.contains(" ")
        
        return this.length > minLength &&
            hasUpperCase &&
            hasLowerCase &&
            hasDigit &&
            hasSpecialChar &&
            hasNoSpace
        
        // Or Regex (need update to check for space " ")
//        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}\$")
//        return regex.matches(this)
    }
    
    fun String.isValidCaptcha(): Boolean {
        val isLengthValid = this.length == 5
        val isAlphanumeric = this.all { it.isLetterOrDigit() }
        return isLengthValid && isAlphanumeric
        // Or regex
//        val regex = Regex("^[a-zA-Z0-9]{8,}$")
//        return regex.matches(this)
    }
    
}