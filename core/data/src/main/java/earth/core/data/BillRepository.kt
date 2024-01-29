package earth.core.data

import earth.core.database.Bill
import earth.core.database.BillPreview
import earth.core.database.model.BillEntity
import kotlinx.coroutines.flow.Flow

interface BillRepository {
    
    fun getBillsPreview(): Flow<List<BillPreview>>
    fun getBill(billNumber: String): Flow<Bill>
    fun getLastBill(reference: String): Bill?
    suspend fun insertBills(bills: List<Bill>)
    suspend fun deleteBills(reference: String)
    
}