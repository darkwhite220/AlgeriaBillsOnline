package earth.core.database

data class BillPreview(
    val isPaid: Boolean,
    val billNumber: String,
    val date: String,
    val trimester: String,
    val year: String,
    val totalTTC: Int,
)
