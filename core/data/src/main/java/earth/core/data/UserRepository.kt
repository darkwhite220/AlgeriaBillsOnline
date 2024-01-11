package earth.core.data

import earth.core.database.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    
    fun getUsers(): Flow<List<User>>
    fun getUser(reference: String): User?
    suspend fun insertUser(user: User)
    suspend fun deleteUser(reference: String)
    
}