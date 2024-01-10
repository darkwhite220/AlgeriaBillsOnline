package earth.feature.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.database.BillPreview
import earth.core.designsystem.components.MyCircularProgressBar
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.indicatorWidthUnselected
import earth.core.designsystem.components.smallDp
import earth.core.designsystem.components.verticalSpacedBy
import earth.feature.home.components.HomeTopAppBar
import earth.feature.home.components.Indicators
import earth.feature.home.uistate.SyncUiState
import earth.feature.home.uistate.UsersUiState
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@Composable
internal fun HomeRoute(
    onCreateAccountClick: () -> Unit,
    onSignInClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val usersUiState by viewModel.usersUiState.collectAsStateWithLifecycle()
    val syncUiState = viewModel.syncUiState
    
    HomeScreen(
        usersUiState = usersUiState,
        syncUiState = syncUiState,
        onHomeEvent = { event ->
            when (event) {
                HomeEvent.OnCreateAccountClick -> onCreateAccountClick()
                HomeEvent.OnSignInClick -> onSignInClick()
                else -> viewModel.onEvent(event)
            }
        },
    )
    
    LaunchedEffect(key1 = Unit) {
//        viewModel.initSyncData()
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
private fun HomeScreen(
    usersUiState: UsersUiState,
    syncUiState: SyncUiState,
    onHomeEvent: (HomeEvent) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    val pagerState = when (usersUiState) {
        is UsersUiState.Successful -> rememberPagerState { usersUiState.data.size + 1 }
        else -> rememberPagerState { 1 }
    }
    
    Scaffold(
        modifier = Modifier,
        topBar = {
            HomeTopAppBar(
                canScrollBackward = pagerState.canScrollBackward,
                canScrollForward = pagerState.canScrollForward,
                onBackwardClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                onForwardClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(NavigationBarDefaults.windowInsets)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxSize(),
        ) {
            SyncUi(syncUiState, onHomeEvent)
            
            when (usersUiState) {
                UsersUiState.Loading -> {
                    MyCircularProgressBar()
                }
                is UsersUiState.Successful -> {
                    Log.d(TAG, "${usersUiState.data}")
                    
                    HorizontalPager(
                        state = pagerState,
                        verticalAlignment = Alignment.Top,
                        key = { item -> item },
                        modifier = Modifier.weight(1f)
                    ) { index ->
                        
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (index < usersUiState.data.size) {
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
                                        // Indicators Height (circle)
                                        item {
                                            MyHeightSpacer(indicatorWidthUnselected)
                                        }
                                    }
                                }
                            } else {
                                NewUserPage(onHomeEvent = onHomeEvent)
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = smallDp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Indicators(
                size = pagerState.pageCount,
                index = pagerState.currentPage
            )
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

@Composable
private fun NewUserPage(onHomeEvent: (HomeEvent) -> Unit) {
    Text(text = "Welcome new user")
    Button(onClick = { onHomeEvent(HomeEvent.OnCreateAccountClick) }) {
        Text(text = "Create New Account")
    }
    Button(onClick = {
        onHomeEvent(HomeEvent.OnSignInClick)
    }) {
        Text(text = "SignIn")
    }
}

@Composable
private fun SyncUi(
    syncUiState: SyncUiState,
    onHomeEvent: (HomeEvent) -> Unit
) {
    when (syncUiState) {
        SyncUiState.InitialState -> {}
        SyncUiState.Loading -> {
            MyCircularProgressBar()
        }
        is SyncUiState.Success -> {
            Log.d(TAG, "HomeScreen: syncData updated")
            if (syncUiState.isNotEmpty) {
                Toast.makeText(LocalContext.current, "SyncData updated", Toast.LENGTH_SHORT)
                    .show()
                onHomeEvent(HomeEvent.OnSuccessSyncUiState)
            }
        }
        is SyncUiState.Failed -> {
            Log.d(TAG, "HomeScreen: syncData Failed: ${syncUiState.throwable}")
            Toast.makeText(LocalContext.current, "SyncData Failed", Toast.LENGTH_SHORT)
                .show()
        }
        
    }
}

