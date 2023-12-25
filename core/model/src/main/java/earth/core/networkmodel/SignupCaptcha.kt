package earth.core.networkmodel


data class SignupCaptcha(
    val imageByteArray: ByteArray,
    val length: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as SignupCaptcha
        
        if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        return length == other.length
    }
    
    override fun hashCode(): Int {
        var result = imageByteArray.contentHashCode()
        result = 31 * result + length
        return result
    }
}
