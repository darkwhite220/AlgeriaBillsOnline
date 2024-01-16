package earth.feature.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.database.BillPreview
import earth.core.database.User
import earth.core.designsystem.Util
import earth.core.designsystem.components.MyCircularProgressBar
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.MyLinearProgressBar
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.FAILED
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.FAILED_WRONG_PASSWORD
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.FAILED_WRONG_USERNAME
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.PDF_TEXT_EXTRACTOR
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.TEMPORARILY_LOCKED_ACCOUNT
import earth.core.designsystem.components.dialog.ResponseDialog
import earth.core.designsystem.components.indicatorWidthUnselected
import earth.core.designsystem.components.verticalSpacedBy
import earth.core.throwablemodel.ConvertingPdfThrowable
import earth.core.throwablemodel.SignInThrowable
import earth.feature.home.components.AddAccountPage
import earth.feature.home.components.BottomPagerIndicator
import earth.feature.home.components.HomeTopAppBar
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
        viewModel.initSyncData()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    usersUiState: UsersUiState,
    syncUiState: SyncUiState,
    onHomeEvent: (HomeEvent) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    HomeScreenContent(usersUiState = usersUiState) { data ->
        val users = data.users
        val pagerSize = users.size + 1
        val horizontalState = rememberPagerState { pagerSize }
        val verticalState = rememberPagerState { pagerSize }
        
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopAppBar(
                usersListWrapper = data,
                horizontalState = horizontalState,
                verticalState = verticalState,
                canScrollBackward = horizontalState.canScrollBackward,
                canScrollForward = horizontalState.canScrollForward,
                onBackwardClick = {
                    scope.launch {
                        horizontalState.animateScrollToPage(horizontalState.currentPage - 1)
                    }
                },
                onForwardClick = {
                    scope.launch {
                        horizontalState.animateScrollToPage(horizontalState.currentPage + 1)
                    }
                }
            )
            
            HorizontalPager(
                state = horizontalState,
                key = { item -> item },
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                if (index < users.size) {
                    users[index].billsPreview?.let { billPreview ->
                        LazyColumn(
                            verticalArrangement = verticalSpacedBy(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Sync data progress bar
                            item {
                                SyncUi(syncUiState, onHomeEvent)
                            }
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
                    AddAccountPage(onHomeEvent = onHomeEvent)
                }
            }
        }
        BottomPagerIndicator(horizontalState)
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
private fun HomeScreenContent(
    usersUiState: UsersUiState,
    content: @Composable (UsersListWrapper) -> Unit
) {
    when (usersUiState) {
        is UsersUiState.Successful -> {
            content(UsersListWrapper(usersUiState.data))
        }
        UsersUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MyCircularProgressBar()
            }
        }
    }
}

@Composable
private fun SyncUi(
    syncUiState: SyncUiState,
    onHomeEvent: (HomeEvent) -> Unit
) {
    val context = LocalContext.current
    when (syncUiState) {
        SyncUiState.InitialState -> {}
        SyncUiState.Loading -> {
            MyLinearProgressBar()
        }
        is SyncUiState.Success -> {
            syncUiState.isNewBills?.let { isNewBills ->
                val message = if (isNewBills) {
                    R.string.new_bill_found
                } else {
                    R.string.no_new_bill_found
                }
                Toast.makeText(context, stringResource(message), Toast.LENGTH_SHORT).show()
            }
            onHomeEvent(HomeEvent.OnSuccessSyncUiState)
        }
        is SyncUiState.Failed -> {
            Log.d(TAG, "HomeScreen: syncData Failed: ${syncUiState.throwable}")
            var supportMessage: String? = null
            val dialogDataType: HomeScreenFailedResponseDialog = when (syncUiState.throwable) {
                SignInThrowable.BadPassword -> FAILED_WRONG_PASSWORD
                SignInThrowable.BadUsername -> FAILED_WRONG_USERNAME
                SignInThrowable.TemporarilyLockedAccount -> TEMPORARILY_LOCKED_ACCOUNT
                ConvertingPdfThrowable.PdfTextExtractorError -> PDF_TEXT_EXTRACTOR
                else -> {
                    if (syncUiState.throwable is ConvertingPdfThrowable.BadPdfFormat ||
                        syncUiState.throwable is ConvertingPdfThrowable.UnhandledSignInResponse
                    ) {
                        supportMessage = syncUiState.throwable.message
                    }
                    FAILED
                }
            }
            ResponseDialog(
                dialogData = dialogDataType.dialogData,
                supportMessage = supportMessage,
                onDismissClick = { onHomeEvent(HomeEvent.OnFailedSyncUiState) },
                onContactSupportClick = {
                    Util.sendEmail(
                        context = context,
                        titleId = R.string.sync_data_error,
                        message = supportMessage
                    )
                    onHomeEvent(HomeEvent.OnFailedSyncUiState)
                },
            )
        }
    }
}

@Immutable
data class UsersListWrapper(val users: List<User>)