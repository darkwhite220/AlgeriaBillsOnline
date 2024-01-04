package earth.feature.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.designsystem.components.FABCreateAccount
import earth.core.designsystem.components.MyCircularProgressBar
import earth.core.designsystem.components.topappbar.HomeTopAppBar
import earth.feature.home.uistate.UsersUiState
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@Composable
internal fun HomeRoute(
    onCreateAccountClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val usersUiState by viewModel.usersUiState.collectAsStateWithLifecycle()
    
    HomeScreen(
        usersUiState = usersUiState,
        onCreateAccountClick = onCreateAccountClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun HomeScreen(
    usersUiState: UsersUiState,
    onCreateAccountClick: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    
    Scaffold(
        modifier = Modifier,
        topBar = {
            HomeTopAppBar()
        },
        floatingActionButton = {
            FABCreateAccount(onCreateAccountClick)
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(NavigationBarDefaults.windowInsets)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxSize(),
        ) {
            Text(text = "HOME")
            when (usersUiState) {
                UsersUiState.Loading -> {
                    MyCircularProgressBar()
                }
                is UsersUiState.Successful -> {
                    Log.d(TAG, "${usersUiState.data}")
                }
            }
        }
    }
    
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
        ) {
            // Sheet content
            Text(text = "navController.popBackStack()")
            Text(text = "navController.popBackStack()")
            Text(text = "navController.popBackStack()")
            Button(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }) {
                Text("Hide bottom sheet")
            }
        }
    }
    
}

