package earth.feature.settings

import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.LanguageConfig
import earth.core.preferencesmodel.ThemeBrand

sealed interface SettingsEvent {
    data class OnDarKThemeChange(val darkThemeConfig: DarkThemeConfig) : SettingsEvent
    data class OnThemeBrandChange(val themeBrand: ThemeBrand) : SettingsEvent
    data class OnNotificationChange(val isEnabled: Boolean) : SettingsEvent
    data class OnLanguageChange(val language: LanguageConfig) : SettingsEvent
    data object OnFirstLaunch : SettingsEvent
}