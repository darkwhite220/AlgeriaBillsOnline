package earth.feature.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.designsystem.components.MyDivider
import earth.core.designsystem.components.SettingsDialogSectionTitle
import earth.core.designsystem.components.customContentSizeAnimation
import earth.core.designsystem.components.extraLargeDp
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.core.designsystem.icon.AppIcons
import earth.core.preferencesmodel.DarkThemeConfig.DARK
import earth.core.preferencesmodel.DarkThemeConfig.FOLLOW_SYSTEM
import earth.core.preferencesmodel.DarkThemeConfig.LIGHT
import earth.core.preferencesmodel.LanguageConfig.ARABIC
import earth.core.preferencesmodel.LanguageConfig.ENGLISH
import earth.core.preferencesmodel.LanguageConfig.FRENCH
import earth.core.preferencesmodel.ThemeBrand.ANDROID
import earth.core.preferencesmodel.ThemeBrand.DEFAULT
import earth.core.preferencesmodel.UserData
import earth.feature.settings.componenets.PermissionRequestRationalDialog
import earth.feature.settings.componenets.SettingsAboutPanel
import earth.feature.settings.componenets.SettingsDialogOptionChooserRow
import earth.feature.settings.componenets.SettingsDialogRow
import earth.feature.settings.componenets.SettingsHeader

@Composable
internal fun SettingsRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onSettingsEvent = viewModel::setUserData,
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onSettingsEvent: (SettingsEvent) -> Unit,
) {
    Column {
        CenteredTopAppBar(
            titleId = R.string.settings,
            onBackClick = onBackClick
        )
        
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = largeDp, vertical = mediumDp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            SettingsHeader()
            
            SettingsDialogSectionTitle(R.string.app)
            when (uiState) {
                SettingsUiState.Loading -> Text(text = stringResource(R.string.settings_loading))
                is SettingsUiState.Success -> {
                    SettingsAppPanel(
                        userData = uiState.userData,
                        onSettingsEvent = onSettingsEvent,
                    )
                }
            }
            
            SettingsDialogSectionTitle(R.string.settings_about)
            SettingsAboutPanel()
        }
    }
}

@Composable
fun SettingsAppPanel(
    userData: UserData,
    onSettingsEvent: (SettingsEvent) -> Unit,
) {
    val context = LocalContext.current
    var expandDarkTheme by remember { mutableStateOf(false) }
    var expandThemeBrand by remember { mutableStateOf(false) }
    var expandLanguage by remember { mutableStateOf(false) }
    var shouldShowPermissionRational by remember { mutableStateOf(false) }
    val angleTheme: Float by animateFloatAsState(
        targetValue = if (expandDarkTheme) -180F else 0f, label = "angleTheme"
    )
    val angleBrand: Float by animateFloatAsState(
        targetValue = if (expandThemeBrand) -180F else 0f, label = "angleBrand"
    )
    val angleLanguage: Float by animateFloatAsState(
        targetValue = if (expandLanguage) -180F else 0f, label = "angleLanguage"
    )
    
    OnFirstLaunch(
        settings = userData,
        onFirstLaunch = { onSettingsEvent(SettingsEvent.OnFirstLaunch) }
    )
    
    val notificationPermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !isGranted && !userData.notification
            ) {
                shouldShowPermissionRational = true
            }
            onSettingsEvent(SettingsEvent.OnNotificationChange(isGranted))
        }
    
    Card(
        modifier = Modifier.customContentSizeAnimation(),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            // DarkTheme
            SettingsDialogRow(
                startingIcon = AppIcons.DarkTheme,
                title = R.string.dark_theme_pref,
                angle = angleTheme,
                selected = when (userData.darkThemeConfig) {
                    FOLLOW_SYSTEM -> R.string.dark_mode_config_system_default
                    LIGHT -> R.string.dark_mode_config_light
                    DARK -> R.string.dark_mode_config_dark
                },
                onClick = { expandDarkTheme = !expandDarkTheme }
            )
            AnimatedVisibility(visible = expandDarkTheme) {
                Column(
                    modifier = Modifier
                        .selectableGroup()
                        .padding(horizontal = mediumDp)
                ) {
                    SettingsDialogOptionChooserRow(
                        text = stringResource(R.string.dark_mode_config_system_default),
                        selected = userData.darkThemeConfig == FOLLOW_SYSTEM,
                        onClick = { onSettingsEvent(SettingsEvent.OnDarKThemeChange(FOLLOW_SYSTEM)) }
                    )
                    SettingsDialogOptionChooserRow(
                        text = stringResource(R.string.dark_mode_config_light),
                        selected = userData.darkThemeConfig == LIGHT,
                        onClick = { onSettingsEvent(SettingsEvent.OnDarKThemeChange(LIGHT)) }
                    )
                    SettingsDialogOptionChooserRow(
                        text = stringResource(R.string.dark_mode_config_dark),
                        selected = userData.darkThemeConfig == DARK,
                        onClick = { onSettingsEvent(SettingsEvent.OnDarKThemeChange(DARK)) }
                    )
                }
            }
            MyDivider()
            
            // BrandTheme
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                SettingsDialogRow(
                    startingIcon = AppIcons.ThemeBrand,
                    title = R.string.theme,
                    angle = angleBrand,
                    selected = when (userData.themeBrand) {
                        DEFAULT -> R.string.theme_default
                        ANDROID -> R.string.theme_android
                    },
                    onClick = { expandThemeBrand = !expandThemeBrand }
                )
                AnimatedVisibility(visible = expandThemeBrand) {
                    Column(
                        modifier = Modifier
                            .selectableGroup()
                            .padding(horizontal = mediumDp)
                    ) {
                        SettingsDialogOptionChooserRow(
                            text = stringResource(R.string.theme_default),
                            selected = userData.themeBrand == DEFAULT,
                            onClick = { onSettingsEvent(SettingsEvent.OnThemeBrandChange(DEFAULT)) }
                        )
                        SettingsDialogOptionChooserRow(
                            text = stringResource(R.string.theme_android),
                            selected = userData.themeBrand == ANDROID,
                            onClick = { onSettingsEvent(SettingsEvent.OnThemeBrandChange(ANDROID)) }
                        )
                    }
                }
                MyDivider()
            }
            
            // Language
            SettingsDialogRow(
                startingIcon = AppIcons.Language,
                title = R.string.app_language,
                angle = angleLanguage,
                selected = when (userData.language) {
                    ENGLISH -> R.string.en_lang
                    FRENCH -> R.string.fr_lang
                    ARABIC -> R.string.ar_lang
                },
                onClick = { expandLanguage = !expandLanguage }
            )
            AnimatedVisibility(visible = expandLanguage) {
                Column(
                    modifier = Modifier
                        .selectableGroup()
                        .padding(horizontal = mediumDp)
                ) {
                    SettingsDialogOptionChooserRow(
                        text = stringResource(R.string.en_lang),
                        selected = userData.language == ENGLISH,
                        onClick = { onSettingsEvent(SettingsEvent.OnLanguageChange(ENGLISH)) }
                    )
                    SettingsDialogOptionChooserRow(
                        text = stringResource(R.string.fr_lang),
                        selected = userData.language == FRENCH,
                        onClick = { onSettingsEvent(SettingsEvent.OnLanguageChange(FRENCH)) }
                    )
                    SettingsDialogOptionChooserRow(
                        text = stringResource(R.string.ar_lang),
                        selected = userData.language == ARABIC,
                        onClick = { onSettingsEvent(SettingsEvent.OnLanguageChange(ARABIC)) }
                    )
                }
            }
            MyDivider()
            
            // Notification
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontalSpacedBy(10.dp),
                modifier = Modifier.padding(start = 10.dp, top = 2.dp, end = 0.dp, bottom = 2.dp)
            ) {
                Icon(
                    modifier = Modifier.size(extraLargeDp),
                    painter = painterResource(AppIcons.Notification),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.app_notifications),
                    modifier = Modifier.weight(1f)
                )
                androidx.compose.material.Switch(
                    checked = userData.notification,
                    onCheckedChange = { newValue ->
                        if (askNotificationPermission(context, notificationPermission)) {
                            onSettingsEvent(SettingsEvent.OnNotificationChange(newValue))
                        }
                    },
                    modifier = Modifier.padding(end = mediumDp)
                )
            }
        }
    }
    
    if (shouldShowPermissionRational) {
        PermissionRequestRationalDialog(onClick = { shouldShowPermissionRational = false })
    }
}

@Composable
fun OnFirstLaunch(settings: UserData, onFirstLaunch: () -> Unit) {
    if (settings.firstLaunch && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        onFirstLaunch()
    }
}

/**
 * Ask notification permission if needed and update preferences in "rememberLauncherForActivityResult" result,
 * else update on switch onCheckChange
 */
fun askNotificationPermission(
    context: Context,
    request: ManagedActivityResultLauncher<String, Boolean>
): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            request.launch(Manifest.permission.POST_NOTIFICATIONS)
            false
        } else
            true
    } else
        true
}