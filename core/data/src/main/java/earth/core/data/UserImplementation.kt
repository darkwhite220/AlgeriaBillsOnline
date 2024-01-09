package earth.core.data

import earth.core.database.User
import earth.core.database.dao.UserDao
import earth.core.database.model.asEntity
import earth.core.database.model.asExternalModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserImplementation @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    
    override fun getUsers(): Flow<List<User>> =
        userDao.getUsersFlow().map {
            it.map { userEntity -> userEntity.asExternalModel() }
        }
    
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user.asEntity())
    }
    
    override suspend fun deleteUser(reference: String) =
        userDao.deleteUser(reference)
    
}