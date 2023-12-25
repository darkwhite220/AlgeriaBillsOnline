package earth.feature.settings

import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.ThemeBrand

sealed interface SettingsEvent {
  data class OnDarKThemeChange(val darkThemeConfig: DarkThemeConfig): SettingsEvent
  data class OnThemeBrandChange(val themeBrand: ThemeBrand): SettingsEvent
}