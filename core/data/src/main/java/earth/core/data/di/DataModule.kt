package earth.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import earth.core.data.BillImplementation
import earth.core.data.BillRepository
import earth.core.data.EstimateImplementation
import earth.core.data.EstimateRepository
import earth.core.data.SignInImplementation
import earth.core.data.SignInRepository
import earth.core.data.SignupImplementation
import earth.core.data.SignupRepository
import earth.core.data.SyncDataImplementation
import earth.core.data.SyncDataRepository
import earth.core.data.UserDataImplementation
import earth.core.data.UserDataRepository
import earth.core.data.UserImplementation
import earth.core.data.UserRepository
import earth.core.data.util.NetworkMonitorImplementation
import earth.core.data.util.NetworkMonitorRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    
    @Binds
    fun bindUserDataRepository(impl: UserDataImplementation): UserDataRepository
    
    @Binds
    fun bindNetworkConnectivity(impl: NetworkMonitorImplementation): NetworkMonitorRepository
    
    @Binds
    fun bindCreateAccountRepository(impl: SignupImplementation): SignupRepository
    
    @Binds
    fun bindUserRepository(impl: UserImplementation): UserRepository
    
    @Binds
    fun bindBillRepository(impl: BillImplementation): BillRepository
    
    @Binds
    fun bindSyncDataRepository(impl: SyncDataImplementation): SyncDataRepository
    
    @Binds
    fun bindSignInRepository(impl: SignInImplementation): SignInRepository
    
    @Binds
    fun bindEstimateRepository(impl: EstimateImplementation): EstimateRepository
}