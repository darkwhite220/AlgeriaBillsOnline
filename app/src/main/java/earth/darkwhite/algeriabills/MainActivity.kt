package earth.darkwhite.algeriabills

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import earth.core.data.NetworkMonitorRepository
import earth.core.designsystem.theme.AlgeriaBillsTheme
import earth.core.designsystem.utils.Constants.ARABIC_LANGUAGE_TAG
import earth.core.designsystem.utils.Constants.ENGLISH_LANGUAGE_TAG
import earth.core.designsystem.utils.Constants.FRENCH_LANGUAGE_TAG
import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.LanguageConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.darkwhite.algeriabills.ui.AlgeriaBillsApp
import earth.darkwhite.algeriabills.worker.SyncDataWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    @Inject
    lateinit var networkMonitor: NetworkMonitorRepository
    
    private val viewModel: MainActivityViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        initSyncDataWorker()
        
        enableEdgeToEdge()
        
        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)
        
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiState.onEach {
                    uiState = it
                }.collect()
            }
        }
        
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }
        
        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)
            AppLocales(uiState)
            
            AlgeriaBillsTheme(
                darkTheme = darkTheme,
                dynamicColor = shouldUseAndroidTheme(uiState)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AlgeriaBillsApp(
                        networkMonitor = networkMonitor
                    )
                }
            }
        }
    }
    
    private fun initSyncDataWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val work = PeriodicWorkRequestBuilder<SyncDataWorker>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .setInitialDelay(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        
        WorkManager.getInstance(this.applicationContext)
            .enqueueUniquePeriodicWork(
                "SyncDataWork",
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
    }
}

@Composable
fun shouldUseAndroidTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}

@Composable
fun shouldUseDarkTheme(
    uiState: MainActivityUiState
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

@Composable
fun AppLocales(uiState: MainActivityUiState) {
    val language = when (uiState) {
        MainActivityUiState.Loading -> AppCompatDelegate.getApplicationLocales().toLanguageTags()
        is MainActivityUiState.Success -> when (uiState.userData.language) {
            LanguageConfig.ENGLISH -> ENGLISH_LANGUAGE_TAG
            LanguageConfig.FRENCH -> FRENCH_LANGUAGE_TAG
            LanguageConfig.ARABIC -> ARABIC_LANGUAGE_TAG
        }
    }
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(language)
    )
//    return if (language == ARABIC_LANGUAGE_TAG) LayoutDirection.Rtl else LayoutDirection.Ltr
}
