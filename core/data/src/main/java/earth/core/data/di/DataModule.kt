package earth.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import earth.core.data.SignupImplementation
import earth.core.data.SignupRepository
import earth.core.data.UserDataImplementation
import earth.core.data.UserDataRepository
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
}