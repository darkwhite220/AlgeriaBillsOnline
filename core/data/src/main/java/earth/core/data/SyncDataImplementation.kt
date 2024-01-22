package earth.core.data

import android.util.Log
import earth.core.data.util.Constants.DELAY_BETWEEN_EACH_SYNC_REQUEST
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
import kotlinx.coroutines.delay

class SyncDataImplementation @Inject constructor(
    private val appNetwork: AppNetworkDataSource,
    private val userDao: UserDao,
    private val billDao: BillDao,
) : SyncDataRepository {
    
    override suspend fun syncData(): Boolean? {
        val userList: List<User> = userDao.getUsers().map { it.asExternalModel() }
        var result: Boolean? = null
        
        userList.forEach { user ->
            var newUserCopy = user
            // login
            val signInResponse = extractSignInPageData(
                appNetwork.signIn(
                    username = user.username,
                    password = user.password
                )
            )
            Log.d(TAG, "syncData: $signInResponse")
            
            // Save user data from logged in page
            if (user.fullName.isEmpty()) {
                newUserCopy = newUserCopy.copy(
                    fullName = signInResponse.fullName,
                    address = signInResponse.address,
                    lastBillNumber = signInResponse.billNumber,
                )
                
                userDao.insertUser(newUserCopy.asEntity())
            }
            
            val lastBill = billDao.getLastBill(user.reference)?.asExternalModel()
            Log.d(TAG, "syncData: lastBill $lastBill")
            
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
                    var gazPCS = "0".toBigDecimal()
                    
                    // Extract Main Bill
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
                    Log.d(TAG, "syncData: new mainBillData = ${billsList[0]} ")
                    
                    // Extract Previous Bills
                    billsList.addAll(
                        extractPreviousBillsData(
                            dataSource = dataAsListOfString,
                            mainBill = billsList[0],
                            menageType = menageType,
                            electricityPMD = electricityPMD,
                            gazPCS = gazPCS,
                            isPaid = true,
                            onDone = { pcs ->
                                if (gazPCS == "0".toBigDecimal()) {
                                    gazPCS = pcs
                                }
                            }
                        )
                    )
                    
                    // Save new user data
                    // gasPCS can be 0 when no consumption registered
                    if (user.gasPCS != gazPCS) {
                        newUserCopy = newUserCopy.copy(
                            directionDistribution = direction,
                            businessAgency = agency,
                            isHouse = menageType in listOf(MenageType.M, MenageType.M_OUT_CITY),
                            isInState = menageType in listOf(MenageType.M, MenageType.NM),
                            electPMD = electricityPMD.value,
                            gasPCS = gazPCS,
                        )
                        
                        userDao.insertUser(newUserCopy.asEntity())
                    }
                    
                    // Extract & Insert bills
                    billDao.insertBills(
                        billsList.map { it.asEntity() }
                    )
                }
                result = true
            } else {
                result = false
            }
            appNetwork.logOut()
            delay(DELAY_BETWEEN_EACH_SYNC_REQUEST)
        }
        // TODO: return reference of throwable initiators & push to the Ui
        return result
    }
    
    companion object {
        private const val TAG = "SyncDataImplementation"
    }
}