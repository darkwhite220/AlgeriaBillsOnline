package earth.core.database

data class BillDownload(
    val reference: String,
    val trimester: String,
    val year: String,
    val byteArray: ByteArray,
) {
    fun toFileName(): String =
        "T${trimester}_${year}_${reference}.pdf"
}
