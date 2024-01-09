package earth.core.data

import android.util.Log
import earth.core.data.util.ExtractionUtil.extractMainBillData
import earth.core.data.util.ExtractionUtil.extractPreviousBillsData
import earth.core.data.util.PdfUtil.extractDataFromByteArray
import earth.core.data.util.SignInUtil.extractSignInPageData
import earth.core.database.Bill
import earth.core.database.ElectricityPMD
import earth.core.database.MenageType
import earth.core.database.User
import earth.core.database.dao.BillDao
import earth.core.database.dao.UserDao
import earth.core.database.model.asEntity
import earth.core.database.model.asExternalModel
import earth.core.network.AppNetworkDataSource
import javax.inject.Inject

class SyncDataImplementation @Inject constructor(
    private val appNetwork: AppNetworkDataSource,
    private val userDao: UserDao,
    private val billDao: BillDao,
) : SyncDataRepository {
    
    override suspend fun syncData(): Boolean {
        val userList: List<User> = userDao.getUsers().map { it.asExternalModel() }
        var result = false
        
        userList.forEach { user ->
            // login
            val signInResponse = extractSignInPageData(
                appNetwork.signIn(
                    username = user.username,
                    password = user.password
                )
            )
            Log.d(TAG, "syncData: $signInResponse")
            
            // Save user data from logged in page
            if (user.fullName.isNotEmpty()) {
                userDao.insertUser(
                    user.asEntity().copy(
                        fullName = signInResponse.fullName,
                        address = signInResponse.address,
                        lastBillNumber = signInResponse.billNumber,
                    )
                )
            }
            
            val lastBill = billDao.getLastBill(user.reference)?.asExternalModel()
            
            if (user.lastBillNumber != signInResponse.billNumber ||
                lastBill == null ||
                lastBill.billNumber != signInResponse.billNumber
            ) {
                // fetch bill
                signInResponse.billUrl?.let { urlEndpoint ->
                    val billResponse = appNetwork.fetchBill(urlEndpoint)
                    
                    var dataAsListOfString: List<String> = extractDataFromByteArray(
                        byteArray = billResponse.pdfByteArray
                    )
                    
                    val billsList = mutableListOf<Bill>()
                    var direction = ""
                    var agency = ""
                    var menageType = MenageType.M
                    var electricityPMD = ElectricityPMD.MEDIUM_MONO_PHASE
                    var gazPCS = "8.8".toBigDecimal()
                    
                    // Main Bill
                    billsList.add(
                        extractMainBillData(
                            dataSource = dataAsListOfString,
                            pdfByteArray = billResponse.pdfByteArray,
                            isPaid = lastBill == null,
                            onDone = { directionDistribution, agence, menage, pmd, pcs ->
                                direction = directionDistribution
                                agency = agence
                                menageType = menage
                                electricityPMD = pmd
                                gazPCS = pcs
                                dataAsListOfString = dataAsListOfString.subList(
                                    dataAsListOfString.size - 19,
                                    dataAsListOfString.size
                                )
                            }
                        )
                    )
                    
                    // Save new user data
                    if (user.gasPCS != gazPCS) {
                        userDao.insertUser(
                            user.asEntity().copy(
                                directionDistribution = direction,
                                businessAgency = agency,
                                isHouse = menageType in listOf(MenageType.M, MenageType.M_OUT_CITY),
                                isInState = menageType in listOf(MenageType.M, MenageType.NM),
                                electPMD = electricityPMD.value,
                                gasPCS = gazPCS.toString(),
                            )
                        )
                    }
                    
                    // Save previous bills
                    billsList.addAll(
                        extractPreviousBillsData(
                            dataSource = dataAsListOfString,
                            mainBill = billsList[0],
                            menageType = menageType,
                            electricityPMD = electricityPMD,
                            gazPCS = gazPCS,
                            isPaid = true,
                        )
                    )
                    
                    // Extract & Insert bills
                    billDao.insertBills(
                        billsList.map { it.asEntity() }
                    )
                }
            }
            result = true
        }
        return result
    }
    
    companion object {
        private const val TAG = "SyncDataImplementation"
    }
}