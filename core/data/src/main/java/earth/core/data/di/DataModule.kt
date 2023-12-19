package earth.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import earth.core.data.util.NetworkMonitorRepository
import earth.core.data.UserDataImplementation
import earth.core.data.UserDataRepository
import earth.core.data.util.NetworkMonitorImplementation

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
  
  @Binds
  fun bindUserDataRepository(impl: UserDataImplementation): UserDataRepository
  
  @Binds
  abstract fun bindNetworkConnectivity(impl: NetworkMonitorImplementation): NetworkMonitorRepository
}