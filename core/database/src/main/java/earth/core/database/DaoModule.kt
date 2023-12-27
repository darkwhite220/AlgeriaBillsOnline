package earth.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import earth.core.database.dao.BillDao
import earth.core.database.dao.UserDao

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    
    @Provides
    fun provideUserDao(myDatabase: MyDatabase): UserDao =
        myDatabase.userDao()
    
    @Provides
    fun provideBillDao(myDatabase: MyDatabase): BillDao =
        myDatabase.billDao()
}