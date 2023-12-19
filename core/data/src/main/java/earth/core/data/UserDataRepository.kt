package earth.core.data

import earth.core.model.DarkThemeConfig
import earth.core.model.ThemeBrand
import earth.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    
    val userData: Flow<UserData>
    
    suspend fun setShouldHideOnboarding(newValue: Boolean)
    suspend fun setDarkThemeConfig(newValue: DarkThemeConfig)
    suspend fun setThemeBrand(newValue: ThemeBrand)
}