package earth.core.data

import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.core.preferencesmodel.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    
    val userData: Flow<UserData>
    
    suspend fun setShouldHideOnboarding(newValue: Boolean)
    suspend fun setDarkThemeConfig(newValue: DarkThemeConfig)
    suspend fun setThemeBrand(newValue: ThemeBrand)
}