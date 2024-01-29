package earth.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import earth.core.network.AppNetwork
import earth.core.network.AppNetworkDataSource


@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    
    @Binds
    fun bindAppNetwork(impl: AppNetwork): AppNetworkDataSource
}
