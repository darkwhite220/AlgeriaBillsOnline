package earth.core.data

import android.util.Log
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
            val response = appNetwork.signIn(
                username = user.username,
                password = user.password
            )
            Log.d(TAG, "syncData: $response")
        }
        
        // save user data
        // fetch bill
        // save bill
    }
    
    companion object {
        private const val TAG = "SyncDataImplementation"
    }
}