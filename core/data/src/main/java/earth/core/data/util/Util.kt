package earth.core.data.util

import earth.core.networkmodel.SignInResponse
import java.math.BigDecimal
import java.math.RoundingMode


fun BigDecimal.round(): BigDecimal = this.setScale(2, RoundingMode.HALF_EVEN)
fun BigDecimal.roundDown(): BigDecimal = this.setScale(2, RoundingMode.DOWN)
fun BigDecimal.timbreRound(): BigDecimal = this.setScale(0, RoundingMode.UP)

data class CalculationHolder(
    val firstTotalHT: BigDecimal = "0".toBigDecimal(),
    val firstTotalTTC: BigDecimal = "0".toBigDecimal(),
    val firstTVA: BigDecimal = "0".toBigDecimal(),
    val secondTotalHT: BigDecimal = "0".toBigDecimal(),
    val secondTotalTTC: BigDecimal = "0".toBigDecimal(),
    val secondTVA: BigDecimal = "0".toBigDecimal(),
    val totalHT: BigDecimal = "0".toBigDecimal(),
    val totalTVA: BigDecimal = "0".toBigDecimal(),
)

fun SignInResponse.concatThrowableMessage(): String =
    "LOGIN PART".center() + this.signInBody + "\n" + "HOME PAGE PART".center() + this.homePageBody

fun String.center(padding: Int = 10, char: String = "-_-"): String =
    "${char.repeat(padding)}$this${char.repeat(padding)}\n"

fun List<String>.listStringToString(): String =
    "Pdf Format".center() + this.joinToString("\n")
