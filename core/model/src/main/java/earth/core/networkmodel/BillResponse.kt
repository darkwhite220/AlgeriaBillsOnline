package earth.core.networkmodel

data class BillResponse(
    val length: Int,
    val pdfByteArray: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as BillResponse
        
        if (length != other.length) return false
        return pdfByteArray.contentEquals(other.pdfByteArray)
    }
    
    override fun hashCode(): Int {
        var result = length
        result = 31 * result + pdfByteArray.contentHashCode()
        return result
    }
}
