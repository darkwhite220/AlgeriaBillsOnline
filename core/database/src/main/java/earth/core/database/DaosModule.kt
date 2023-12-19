package earth.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import earth.core.database.dao.MyDao

@Module
@InstallIn(SingletonComponent::class)
abstract class DaosModule {
  
  @Provides
  fun provideMyDao(myDatabase: MyDatabase): MyDao =
    myDatabase.myDao()
}