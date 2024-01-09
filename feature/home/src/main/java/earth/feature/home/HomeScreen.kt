package earth.feature.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.database.BillPreview
import earth.core.designsystem.components.FABCreateAccount
import earth.core.designsystem.components.MyCircularProgressBar
import earth.core.designsystem.components.topappbar.HomeTopAppBar
import earth.core.designsystem.components.verticalSpacedBy
import earth.feature.home.uistate.SyncUiState
import earth.feature.home.uistate.UsersUiState
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@Composable
internal fun HomeRoute(
    onCreateAccountClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val usersUiState by viewModel.usersUiState.collectAsStateWithLifecycle()
    val syncUiState by viewModel.syncUiState.collectAsStateWithLifecycle()
    
    HomeScreen(
        usersUiState = usersUiState,
        syncUiState = syncUiState,
        onCreateAccountClick = onCreateAccountClick
    )
    
    LaunchedEffect(key1 = Unit) {
        viewModel.initSyncData()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun HomeScreen(
    usersUiState: UsersUiState,
    syncUiState: SyncUiState,
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
            when (syncUiState) {
                SyncUiState.InitialState -> {}
                SyncUiState.Loading -> {
                    MyCircularProgressBar()
                }
                is SyncUiState.Success -> {
                    Log.d(TAG, "HomeScreen: syncData updated")
                    if (syncUiState.isNotEmpty) {
                        // TODO UPDATE LAST FETCH TIME WHEN CORRECT SUCCESS FETCH
                        Toast.makeText(LocalContext.current, "SyncData updated", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is SyncUiState.Failed -> {
                    Log.d(TAG, "HomeScreen: syncData Failed: ${syncUiState.throwable}")
                    Toast.makeText(LocalContext.current, "SyncData Failed", Toast.LENGTH_SHORT)
                        .show()
                }
                
            }
            Text(text = "HOME")
            when (usersUiState) {
                UsersUiState.Loading -> {
                    MyCircularProgressBar()
                }
                is UsersUiState.Successful -> {
                    Log.d(TAG, "${usersUiState.data}")
                    if (usersUiState.data.isNotEmpty()) {
                        usersUiState.data[0].billsPreview?.let { billPreview ->
                            LazyColumn(
                                verticalArrangement = verticalSpacedBy()
                            ) {
                                items(
                                    items = billPreview,
                                    key = { item: BillPreview -> item.billNumber }
                                ) { item ->
                                    Text(text = "$item")
                                }
                            }
                        }
                    }
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

