package earth.core.data

import earth.core.datastore.PreferencesDataStore
import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.core.preferencesmodel.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserDataImplementation @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : UserDataRepository {
    
    override val userData: Flow<UserData> = preferencesDataStore.userData
    
    override suspend fun setShouldHideOnboarding(newValue: Boolean) {
        preferencesDataStore.setShouldHideOnboarding(newValue)
    }
    
    override suspend fun setDarkThemeConfig(newValue: DarkThemeConfig) {
        preferencesDataStore.setDarkThemeConfig(newValue)
    }
    
    override suspend fun setThemeBrand(newValue: ThemeBrand) {
        preferencesDataStore.setThemeBrand(newValue)
    }
    
}