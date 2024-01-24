package earth.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import earth.core.datastore.PreferencesDataStore.DefaultValues.APP_LANGUAGE_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.DARK_THEME_CONFIG_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.FIRST_LAUNCH_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.LAST_FETCH_TIME_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.NOTIFICATION_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.SHOULD_HIDE_ONBOARDING_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.THEME_BRAND_DEFAULT
import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.LanguageConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.core.preferencesmodel.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) {
    
    val isNotificationEnabled: Flow<Boolean> = userPreferences.data
        .map {
            it[PreferencesKeys.NOTIFICATION] ?: NOTIFICATION_DEFAULT
        }
    
    val lastFetchTime: Flow<Long> = userPreferences.data
        .map {
            it[PreferencesKeys.LAST_FETCH_TIME] ?: LAST_FETCH_TIME_DEFAULT
        }
    
    val userData: Flow<UserData> = userPreferences.data
        .catch {
            Log.e(TAG, "Error: ${it.message}")
        }
        .map {
            UserData(
                shouldHideOnboarding = it[PreferencesKeys.SHOULD_HIDE_ONBOARDING]
                    ?: SHOULD_HIDE_ONBOARDING_DEFAULT,
                darkThemeConfig = DarkThemeConfig.valueOf(
                    it[PreferencesKeys.DARK_THEME_CONFIG] ?: DARK_THEME_CONFIG_DEFAULT
                ),
                themeBrand = ThemeBrand.valueOf(
                    it[PreferencesKeys.THEME_BRAND] ?: THEME_BRAND_DEFAULT
                ),
                language = LanguageConfig.valueOf(
                    it[PreferencesKeys.APP_LANGUAGE] ?: APP_LANGUAGE_DEFAULT
                ),
                notification = it[PreferencesKeys.NOTIFICATION] ?: NOTIFICATION_DEFAULT,
                firstLaunch = it[PreferencesKeys.FIRST_LAUNCH] ?: FIRST_LAUNCH_DEFAULT,
            )
        }
    
    suspend fun setShouldHideOnboarding(newValue: Boolean) {
        userPreferences.edit {
            it[PreferencesKeys.SHOULD_HIDE_ONBOARDING] = newValue
        }
    }
    
    suspend fun setDarkThemeConfig(newValue: DarkThemeConfig) {
        userPreferences.edit {
            it[PreferencesKeys.DARK_THEME_CONFIG] = newValue.name
        }
    }
    
    suspend fun setThemeBrand(newValue: ThemeBrand) {
        userPreferences.edit {
            it[PreferencesKeys.THEME_BRAND] = newValue.name
        }
    }
    
    suspend fun setLastFetchTime(newValue: Long) {
        userPreferences.edit {
            it[PreferencesKeys.LAST_FETCH_TIME] = newValue
        }
    }
    
    suspend fun setNotificationStatus(newValue: Boolean) {
        userPreferences.edit {
            it[PreferencesKeys.NOTIFICATION] = newValue
        }
    }
    
    suspend fun setFirstLaunch(newValue: Boolean) {
        userPreferences.edit {
            it[PreferencesKeys.FIRST_LAUNCH] = newValue
        }
    }
    
    suspend fun setLanguage(newValue: LanguageConfig) {
        userPreferences.edit {
            it[PreferencesKeys.APP_LANGUAGE] = newValue.name
        }
    }
    
    private object PreferencesKeys {
        val SHOULD_HIDE_ONBOARDING = booleanPreferencesKey("should_hide_onboarding")
        val DARK_THEME_CONFIG = stringPreferencesKey("dark_theme_config")
        val THEME_BRAND = stringPreferencesKey("theme_brand")
        val LAST_FETCH_TIME = longPreferencesKey("last_fetch_time")
        val NOTIFICATION = booleanPreferencesKey("notification")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
    }
    
    private object DefaultValues {
        const val SHOULD_HIDE_ONBOARDING_DEFAULT = false
        val DARK_THEME_CONFIG_DEFAULT = DarkThemeConfig.FOLLOW_SYSTEM.name
        val THEME_BRAND_DEFAULT = ThemeBrand.DEFAULT.name
        const val LAST_FETCH_TIME_DEFAULT = 0L
        const val NOTIFICATION_DEFAULT = false
        const val FIRST_LAUNCH_DEFAULT = true
        val APP_LANGUAGE_DEFAULT = LanguageConfig.ENGLISH.name
    }
    
    companion object {
        private const val TAG = "UserPreferences"
    }
}