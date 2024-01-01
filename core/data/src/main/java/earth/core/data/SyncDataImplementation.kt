package earth.core.data

import android.util.Log
import earth.core.database.User
import earth.core.database.dao.BillDao
import earth.core.database.dao.UserDao
import earth.core.database.model.BillEntity
import earth.core.database.model.asEntity
import earth.core.network.AppNetworkDataSource
import javax.inject.Inject

class SyncDataImplementation @Inject constructor(
    private val appNetwork: AppNetworkDataSource,
    private val userDao: UserDao,
    private val billDao: BillDao,
) : SyncDataRepository {
    
    override suspend fun syncData(referenceList: List<User>) {
        referenceList.forEach { user ->
            // login
            val signInResponse = appNetwork.signIn(
                username = user.username,
                password = user.password
            )
            Log.d(TAG, "syncData: $signInResponse")
            
            // save user data
            userDao.insertUser(
                user.asEntity().copy(
                    fullName = signInResponse.fullName,
                    address = signInResponse.address,
                    lastBillNumber = signInResponse.billNumber,
                )
            )
            
            // fetch bill
            signInResponse.billUrl?.let { urlEndpoint ->
                val billResponse = appNetwork.fetchBill(urlEndpoint)
                
                // save bill
                val billsList = mutableListOf<BillEntity>()
                
                
                
                billDao.insertBills(billsList)
            }
        }
        
    }
    
    companion object {
        private const val TAG = "SyncDataImplementation"
    }
}