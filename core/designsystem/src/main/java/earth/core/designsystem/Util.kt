package earth.core.designsystem

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.StringRes
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
    
    fun sendEmail(
        context: Context,
        @StringRes titleId: Int,
        message: String? = null
    ) {
        val email = "dw220appstore+algeriabills@gmail.com"
        val title = context.getString(titleId)
        
        val intentRequest = Intent(Intent.ACTION_SENDTO)
        intentRequest.data = Uri.parse("mailto:$email?subject=$title?text=$message")
        try {
            context.startActivity(Intent.createChooser(intentRequest, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}