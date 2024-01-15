package earth.core.data

interface SyncDataRepository {
    
    suspend fun syncData(): Boolean?
    
}