package earth.core.data

import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.core.preferencesmodel.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    
    val userData: Flow<UserData>
    val lastFetchTime: Flow<Long>
    val isNotificationEnabled: Flow<Boolean>
    
    suspend fun setShouldHideOnboarding(newValue: Boolean)
    suspend fun setDarkThemeConfig(newValue: DarkThemeConfig)
    suspend fun setThemeBrand(newValue: ThemeBrand)
    suspend fun setLastFetchTime(newValue: Long)
}