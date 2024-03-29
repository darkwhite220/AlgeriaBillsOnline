package earth.core.data

import earth.core.database.Bill
import earth.core.database.BillPreview
import earth.core.database.dao.BillDao
import earth.core.database.model.asEntity
import earth.core.database.model.asExternalModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BillImplementation @Inject constructor(
    private val billDao: BillDao
) : BillRepository {
    
    override fun getBillsPreview(): Flow<List<BillPreview>> =
        billDao.getBillsPreview().map {
            it.map { billPreviewEntity -> billPreviewEntity.asExternalModel() }
        }
    
    override fun getBill(billNumber: String): Flow<Bill> =
        billDao.getBill(billNumber).map { it.asExternalModel() }
    
    override fun getLastBill(reference: String): Bill? =
        billDao.getLastBill(reference)?.asExternalModel()
    
    override suspend fun insertBills(bills: List<Bill>) =
        billDao.insertBills(bills.map { it.asEntity() })
    
    override suspend fun deleteBills(reference: String) =
        billDao.deleteBills(reference)
    
}