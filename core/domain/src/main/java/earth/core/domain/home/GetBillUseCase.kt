package earth.core.domain.home

import earth.core.data.BillRepository
import earth.core.database.Bill
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetBillUseCase @Inject constructor(
    private val billRepository: BillRepository
) {
    
    operator fun invoke(billNumber: String): Flow<Bill> =
        billRepository.getBill(billNumber)
    
}