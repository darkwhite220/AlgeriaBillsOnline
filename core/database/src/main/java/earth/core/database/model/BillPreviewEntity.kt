package earth.core.database.model

import androidx.room.ColumnInfo
import earth.core.database.BillPreview

data class BillPreviewEntity(
    @ColumnInfo(name = "is_paid")
    val isPaid: Boolean,
    @ColumnInfo(name = "bill_number")
    val billNumber: String,
    val date: String,
    val trimester: String,
    val year: String,
    @ColumnInfo(name = "total_ttc")
    val totalTTC: Int,
)

fun BillPreviewEntity.asExternalModel() = BillPreview(
    isPaid = isPaid,
    billNumber = billNumber,
    date = date,
    trimester = trimester,
    year = year,
    totalTTC = totalTTC,
)