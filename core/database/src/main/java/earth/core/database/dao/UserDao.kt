package earth.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import earth.core.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM user_table ORDER BY `created_at` ASC")
    fun getUsersFlow(): Flow<List<UserEntity>>
    
    @Query("SELECT * FROM user_table ORDER BY `created_at` ASC")
    fun getUsers(): List<UserEntity>
    
    @Query("SELECT * FROM user_table WHERE reference LIKE :reference")
    fun getUser(reference: String): UserEntity?
    
    @Upsert
    suspend fun insertUser(userEntity: UserEntity)
    
    @Query("DELETE FROM user_table WHERE reference LIKE :reference")
    suspend fun deleteUser(reference: String)
    
}