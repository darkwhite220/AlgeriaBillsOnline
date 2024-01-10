package earth.core.designsystem

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import earth.core.designsystem.Constants.CAPTCHA_LENGTH
import earth.core.designsystem.Constants.MAX_REFERENCE_LENGTH
import earth.core.designsystem.Constants.MIN_PASSWORD_LENGTH
import earth.core.designsystem.Constants.MIN_USERNAME_LENGTH

object Util {
    
    fun String.isValidUsername(): Boolean {
        val isLengthValid = this.length > MIN_USERNAME_LENGTH
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
        return this.length == MAX_REFERENCE_LENGTH &&
            this.isDigitsOnly()
    }
    
    fun String.isValidPassword(): Boolean {
        val hasUpperCase = this.any { it.isUpperCase() }
        val hasLowerCase = this.any { it.isLowerCase() }
        val hasDigit = this.any { it.isDigit() }
        val hasSpecialChar = this.any { !it.isLetterOrDigit() }
        val hasNoSpace = !this.contains(" ")
        
        return this.length > MIN_PASSWORD_LENGTH &&
            hasUpperCase &&
            hasLowerCase &&
            hasDigit &&
            hasSpecialChar &&
            hasNoSpace
        
        // Or Regex (need update to check for space " ")
//        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}\$")
//        return regex.matches(this)
    }
    
    fun String.isValidSignInPassword(): Boolean {
        val hasLowerCase = this.any { it.isLowerCase() }
        val hasDigit = this.any { it.isDigit() }
        val hasSpecialChar = this.any { !it.isLetterOrDigit() }
        val hasNoSpace = !this.contains(" ")
        
        return this.length > MIN_PASSWORD_LENGTH &&
            hasLowerCase &&
            hasDigit &&
            hasSpecialChar &&
            hasNoSpace
        
        // Or Regex (need update to check for space " ")
//        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}\$")
//        return regex.matches(this)
    }
    
    fun String.isValidCaptcha(): Boolean {
        val isLengthValid = this.length == CAPTCHA_LENGTH
        val isAlphanumeric = this.all { it.isLetterOrDigit() }
        return isLengthValid && isAlphanumeric
        // Or regex
//        val regex = Regex("^[a-zA-Z0-9]{8,}$")
//        return regex.matches(this)
    }
    
}