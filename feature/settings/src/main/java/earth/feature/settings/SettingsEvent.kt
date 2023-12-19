package earth.feature.settings

import earth.core.model.DarkThemeConfig
import earth.core.model.ThemeBrand

sealed interface SettingsEvent {
  data class OnDarKThemeChange(val darkThemeConfig: DarkThemeConfig): SettingsEvent
  data class OnThemeBrandChange(val themeBrand: ThemeBrand): SettingsEvent
}