package earth.core.data

import earth.core.database.User

interface SyncDataRepository {
    
    suspend fun syncData(referenceList: List<User>)
    
}