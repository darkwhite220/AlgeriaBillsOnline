package earth.core.data

import earth.core.database.User
import earth.core.database.dao.BillDao
import earth.core.database.dao.UserDao
import earth.core.network.AppNetworkDataSource
import javax.inject.Inject

class SyncDataImplementation @Inject constructor(
    private val appNetwork: AppNetworkDataSource,
    private val userDao: UserDao,
    private val billDao: BillDao,
) : SyncDataRepository {
    
    override suspend fun syncData(referenceList: List<User>) {
        // login
        referenceList.forEach { user ->
            appNetwork.login(
                username = user.username,
                password = user.password
            )
        }
        
        // save user data
        // fetch bill
        // save bill
    }
    
}