package earth.core.data

import earth.core.EstimateData
import earth.core.database.Bill

interface EstimateRepository {
    
    fun getEstimateBill(data: EstimateData): Bill
    
}