package earth.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import earth.core.database.model.BillEntity
import earth.core.database.model.BillPreviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    
    @Query(
        """
            SELECT
                is_paid,
                bill_number,
                date,
                trimester,
                year,
                total_ttc,
                reference
            FROM bill_table
            
            ORDER BY bill_number ASC
        """
    )
    fun getBillsPreview(): Flow<List<BillPreviewEntity>>
//    WHERE reference MATCH :reference
//    fun getBillsPreview(reference: String): Flow<List<BillPreviewEntity>>
    
    @Query("SELECT * FROM bill_table WHERE reference MATCH :reference")
    fun getBill(reference: String): Flow<BillEntity>
    
    @Insert
    suspend fun insertBills(billEntity: List<BillEntity>)
    
    @Query("DELETE FROM bill_table WHERE reference MATCH :reference")
    suspend fun deleteBills(reference: String)
}