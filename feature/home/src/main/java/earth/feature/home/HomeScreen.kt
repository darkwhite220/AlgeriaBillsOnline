package earth.feature.home

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import earth.core.database.Bill
import earth.core.database.BillPreview
import earth.core.database.User
import earth.core.designsystem.Util
import earth.core.designsystem.Util.showToast
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
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.components.verticalSpacedBy
import earth.core.throwablemodel.ConvertingPdfThrowable
import earth.core.throwablemodel.SignInThrowable
import earth.feature.home.HomeScreenContentType.BOTTOM_INDICATORS_PADDING
import earth.feature.home.components.AddAccountPage
import earth.feature.home.components.BillBottomSheet
import earth.feature.home.components.BillPreviewItem
import earth.feature.home.components.BottomPagerIndicator
import earth.feature.home.components.HomeTopAppBar
import earth.feature.home.components.OnBillDownloadClick
import earth.feature.home.components.consumptionLevel
import earth.feature.home.uistate.SyncUiState
import earth.feature.home.uistate.UsersUiState
import java.net.UnknownHostException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRoute(
    onCreateAccountClick: () -> Unit,
    onSignInClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    var onBillDownloadClick by remember { mutableStateOf<Bill?>(null) }
    val askPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                viewModel.onEvent(HomeEvent.OnBillDownloadClick(onBillDownloadClick!!))
            } else {
                showToast(context = context, textId = R.string.permission_denied)
            }
        }
    
    val usersUiState by viewModel.usersUiState.collectAsStateWithLifecycle()
    val selectedBill by viewModel.selectedBill.collectAsStateWithLifecycle()
    val syncUiState = viewModel.syncUiState
    val onDownload = viewModel.onDownload
    
    HomeScreen(
        scope = scope,
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
    
    selectedBill?.let { bill ->
        BillBottomSheet(
            sheetState = sheetState,
            bill = bill,
            onHomeEvent = { event ->
                if (event is HomeEvent.OnBillDownloadClick) {
                    onBillDownloadClick = event.bill
                } else {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            viewModel.onEvent(event)
                        }
                    }
                }
            },
        )
    }
    
    onBillDownloadClick?.let {
        OnBillDownloadClick(
            askPermission = askPermission,
            onClick = { viewModel.onEvent(HomeEvent.OnBillDownloadClick(it)) }
        )
    }
    
    if (onDownload.isNotEmpty()) {
        onBillDownloadClick = null
        showToast(
            context = LocalContext.current,
            text = stringResource(R.string.file_saved_download, onDownload)
        )
        viewModel.resetDownloadData()
    }
    
    LaunchedEffect(key1 = Unit) {
        viewModel.initSyncData()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    scope: CoroutineScope,
    usersUiState: UsersUiState,
    syncUiState: SyncUiState,
    onHomeEvent: (HomeEvent) -> Unit = {},
) {
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
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = verticalSpacedBy(),
                            contentPadding = PaddingValues(
                                vertical = mediumDp, horizontal = largeDp
                            )
                        ) {
                            // Sync data progress bar
                            item(contentType = HomeScreenContentType.SYNC_LOADING) {
                                SyncUi(syncUiState, onHomeEvent)
                            }
                            // Bill preview items
                            itemsIndexed(
                                items = billPreview,
                                key = { _, item: BillPreview -> item.billNumber },
                                contentType = { _, _ -> HomeScreenContentType.BILL_PREVIEW_ITEMS },
                            ) { index, item ->
                                BillPreviewItem(
                                    onHomeEvent = onHomeEvent,
                                    item = item,
                                    consumptionLevel = consumptionLevel(
                                        billPreview = billPreview,
                                        index = index,
                                        item = item
                                    )
                                )
                            }
                            // Indicators Height (circle)
                            item(contentType = BOTTOM_INDICATORS_PADDING) {
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
                Log.d(TAG, "SyncUi: isNewBills= $isNewBills")
                showToast(context, message, Toast.LENGTH_SHORT)
            }
            onHomeEvent(HomeEvent.OnSuccessSyncUiState)
        }
        is SyncUiState.Failed -> {
            Log.d(TAG, "HomeScreen: syncData Failed: ${syncUiState.throwable}")
            if (syncUiState.throwable is UnknownHostException) {
                showToast(context, stringResource(R.string.not_connected_to_the_internet))
                return
            }
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

private enum class HomeScreenContentType {
    SYNC_LOADING, BILL_PREVIEW_ITEMS, BOTTOM_INDICATORS_PADDING
}
