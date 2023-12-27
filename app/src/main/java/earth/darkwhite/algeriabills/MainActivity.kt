package earth.darkwhite.algeriabills

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import earth.core.data.util.NetworkMonitorRepository
import earth.core.designsystem.theme.AlgeriaBillsTheme
import earth.core.preferencesmodel.DarkThemeConfig
import earth.core.preferencesmodel.ThemeBrand
import earth.darkwhite.algeriabills.ui.AlgeriaBillsApp
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
        
//        WindowCompat.setDecorFitsSystemWindows(window, false)
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
            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)
            
            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }
            
            AlgeriaBillsTheme(
                darkTheme = darkTheme,
                dynamicColor = shouldUseAndroidTheme(uiState)
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AlgeriaBillsApp(
                        networkMonitor= networkMonitor
                    )
                }
            }
        }
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
