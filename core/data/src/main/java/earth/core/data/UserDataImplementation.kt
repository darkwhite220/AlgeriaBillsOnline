package earth.core.data

import earth.core.datastore.PreferencesDataStore
import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.LanguageConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.core.preferencesmodel.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserDataImplementation @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : UserDataRepository {
    
    override val userData: Flow<UserData> = preferencesDataStore.userData
    override val lastFetchTime: Flow<Long> = preferencesDataStore.lastFetchTime
    override val isNotificationEnabled: Flow<Boolean> = preferencesDataStore.isNotificationEnabled
    
    override suspend fun setShouldHideOnboarding(newValue: Boolean) {
        preferencesDataStore.setShouldHideOnboarding(newValue)
    }
    
    override suspend fun setDarkThemeConfig(newValue: DarkThemeConfig) {
        preferencesDataStore.setDarkThemeConfig(newValue)
    }
    
    override suspend fun setThemeBrand(newValue: ThemeBrand) {
        preferencesDataStore.setThemeBrand(newValue)
    }
    
    override suspend fun setLastFetchTime(newValue: Long) {
        preferencesDataStore.setLastFetchTime(newValue)
    }
    
    override suspend fun setNotificationStatus(newValue: Boolean) {
        preferencesDataStore.setNotificationStatus(newValue)
    }
    
    override suspend fun setFirstLaunch(newValue: Boolean) {
        preferencesDataStore.setFirstLaunch(newValue)
    }
    
    override suspend fun setLanguage(newValue: LanguageConfig) {
        preferencesDataStore.setLanguage(newValue)
    }
    
    override suspend fun setOnBoarding(newValue: Boolean) {
        preferencesDataStore.setOnBoarding(newValue)
    }
    
}