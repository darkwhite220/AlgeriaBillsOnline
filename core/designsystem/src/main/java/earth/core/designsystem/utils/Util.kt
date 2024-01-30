package earth.core.designsystem.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.text.isDigitsOnly
import earth.core.designsystem.R
import earth.core.designsystem.utils.Constants.CAPTCHA_LENGTH
import earth.core.designsystem.utils.Constants.MAX_REFERENCE_LENGTH
import earth.core.designsystem.utils.Constants.MIN_PASSWORD_LENGTH
import earth.core.designsystem.utils.Constants.MIN_USERNAME_LENGTH
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Util {
    
    fun noInternetConnection(context: Context) {
        showToast(context, context.getString(R.string.not_connected_to_the_internet))
    }
    
    fun showToast(context: Context, @StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(
            context,
            context.getString(textId),
            duration
        ).show()
    }
    
    fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(
            context,
            text,
            duration
        ).show()
    }
    
    private val decimalFormat = DecimalFormat("0.00", DecimalFormatSymbols(Locale.ENGLISH))
    
    fun Float.toPrice(): String = decimalFormat.format(this)
    
    fun Int.toPrice(): String = decimalFormat.format(this)
    
    fun BigDecimal.toPrice(): String = decimalFormat.format(this)
    
    fun Int.getNumberSuffix(): Int {
        return when (this) {
            1 -> R.string.trimester_suffix_one
            2 -> R.string.trimester_suffix_two
            3 -> R.string.trimester_suffix_three
            else -> R.string.trimester_suffix_four
        }
    }
    
    fun String.printDate(): String = if (this.isNotEmpty()) " ($this)" else ""
    
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
        intentRequest.data = if (message.isNullOrBlank()) {
            Uri.parse("mailto:$email?subject=$title")
        } else {
            Uri.parse("mailto:$email?subject=$title&body=\nError Message\n$message")
        }
        
        try {
            context.startActivity(Intent.createChooser(intentRequest, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT)
                .show()
        }
    }
    
    fun shareApp(context: Context) {
        val intentShare = Intent(Intent.ACTION_SEND)
        val title = R.string.app_name
        val description = " http://play.google.com/store/apps/details?id=" + context.packageName
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_SUBJECT, title)
        intentShare.putExtra(Intent.EXTRA_TEXT, description)
        context.startActivity(
            Intent.createChooser(
                intentShare,
                context.getString(R.string.share_app)
            )
        )
    }
    
    fun rateApp(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.packageName)
                )
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                )
            )
        }
    }
}