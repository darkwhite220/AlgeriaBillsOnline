package earth.core.preferencesmodel

data class UserData(
    val shouldHideOnboarding: Boolean,
    val darkThemeConfig: DarkThemeConfig,
    val themeBrand: ThemeBrand,
    val language: LanguageConfig,
    val notification: Boolean,
    val firstLaunch: Boolean,
)
