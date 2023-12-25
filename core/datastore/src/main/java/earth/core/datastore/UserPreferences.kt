package earth.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import earth.core.datastore.PreferencesDataStore.DefaultValues.DARK_THEME_CONFIG_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.SHOULD_HIDE_ONBOARDING_DEFAULT
import earth.core.datastore.PreferencesDataStore.DefaultValues.THEME_BRAND_DEFAULT
import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.core.preferencesmodel.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferencesDataStore @Inject constructor(
  private val userPreferences: DataStore<Preferences>
) {
  
  val userData: Flow<UserData> = userPreferences.data
    .catch {
      Log.e(TAG, "Error: ${it.message}")
    }
    .map {
      UserData(
        shouldHideOnboarding = it[PreferencesKeys.SHOULD_HIDE_ONBOARDING] ?: SHOULD_HIDE_ONBOARDING_DEFAULT,
        darkThemeConfig = DarkThemeConfig.valueOf(it[PreferencesKeys.DARK_THEME_CONFIG] ?: DARK_THEME_CONFIG_DEFAULT),
        themeBrand = ThemeBrand.valueOf(it[PreferencesKeys.THEME_BRAND] ?: THEME_BRAND_DEFAULT),
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
  
  private object PreferencesKeys {
    val SHOULD_HIDE_ONBOARDING = booleanPreferencesKey("should_hide_onboarding")
    val DARK_THEME_CONFIG = stringPreferencesKey("dark_theme_config")
    val THEME_BRAND = stringPreferencesKey("theme_brand")
  }
  
  private object DefaultValues {
    const val SHOULD_HIDE_ONBOARDING_DEFAULT = false
    val DARK_THEME_CONFIG_DEFAULT = DarkThemeConfig.FOLLOW_SYSTEM.name
    val THEME_BRAND_DEFAULT = ThemeBrand.DEFAULT.name
  }
  
  companion object {
    private const val TAG = "UserPreferences"
  }
}