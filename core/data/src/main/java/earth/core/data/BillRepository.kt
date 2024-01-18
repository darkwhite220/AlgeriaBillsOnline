package earth.core.data

import earth.core.database.Bill
import earth.core.database.BillPreview
import kotlinx.coroutines.flow.Flow

interface BillRepository {
    
//    fun getBillsPreview(reference: String): Flow<List<BillPreview>>
    fun getBillsPreview(): Flow<List<BillPreview>>
    fun getBill(billNumber: String): Flow<Bill>
    suspend fun insertBills(bills: List<Bill>)
    suspend fun deleteBills(reference: String)
    
}