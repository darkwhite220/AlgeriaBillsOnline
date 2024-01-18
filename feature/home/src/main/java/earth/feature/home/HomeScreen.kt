package earth.feature.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.database.Bill
import earth.core.database.BillPreview
import earth.core.database.User
import earth.core.designsystem.Util
import earth.core.designsystem.components.MyCircularProgressBar
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.MyLinearProgressBar
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.TextTitleMedium
import earth.core.designsystem.components.TextWithEmphasise
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.FAILED
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.FAILED_WRONG_PASSWORD
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.FAILED_WRONG_USERNAME
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.PDF_TEXT_EXTRACTOR
import earth.core.designsystem.components.dialog.HomeScreenFailedResponseDialog.TEMPORARILY_LOCKED_ACCOUNT
import earth.core.designsystem.components.dialog.ResponseDialog
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.indicatorWidthUnselected
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.smallDp
import earth.core.designsystem.components.verticalSpacedBy
import earth.core.throwablemodel.ConvertingPdfThrowable
import earth.core.throwablemodel.SignInThrowable
import earth.feature.home.HomeScreenContentType.BOTTOM_INDICATORS_PADDING
import earth.feature.home.components.AddAccountPage
import earth.feature.home.components.BillPreviewItem
import earth.feature.home.components.BottomPagerIndicator
import earth.feature.home.components.HomeTopAppBar
import earth.feature.home.components.consumptionLevel
import earth.feature.home.uistate.SyncUiState
import earth.feature.home.uistate.UsersUiState
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
    
    val usersUiState by viewModel.usersUiState.collectAsStateWithLifecycle()
    val selectedBill by viewModel.selectedBill.collectAsStateWithLifecycle()
    val syncUiState = viewModel.syncUiState
    
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
            scope = scope,
            bill = bill,
            onHomeEvent = viewModel::onEvent,
        )
    }
    
    LaunchedEffect(key1 = Unit) {
        viewModel.initSyncData()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BillBottomSheet(
    sheetState: SheetState,
    scope: CoroutineScope,
    bill: Bill,
    onHomeEvent: (HomeEvent) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onHomeEvent(HomeEvent.OnBillCloseClick) },
        sheetState = sheetState,
    ) {
        /*TextWithEmphasise(
            modifier = Modifier.fillMaxWidth(),
            text = "Bill Details",
            style = MaterialTheme.typography.bodyLarge,
            alpha = 1f
        )
        Text(text = bill.toString())
        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onHomeEvent(HomeEvent.OnBillCloseClick)
                }
            }
        }) {
            Text("Hide bottom sheet")
        }*/
        TestUseCase_Factory()
    }
}

@Preview(showBackground = true)
@Composable
fun TestUseCase_Factory(modifier: Modifier = Modifier) {
    TextTitleLarge(
        textId = R.string.bill_details,
        modifier = Modifier.fillMaxWidth(),
        fontWeight = FontWeight.SemiBold
    )
    Column(
        modifier = Modifier.padding(largeDp),
        verticalArrangement = verticalSpacedBy()
    ) {
        Text(
            text = "Bill number",
            fontWeight = FontWeight.W400
        )
        Text(
            text = "Period: 1st Trimester 2024 (10.03.2024)",
            fontWeight = FontWeight.W400
        )
        
        val shape = RoundedCornerShape(largeDp)
        TextTitleMedium(
            textId = R.string.electricity,
        )
        Surface(
//            color = Color(155, 186, 233, 205),
            shape = shape,
                tonalElevation = smallDp,
        ) {
            Column(
                modifier = Modifier.padding(largeDp),
                verticalArrangement = verticalSpacedBy(smallDp)
            ) {
                Row {
                    Text(
                        text = "Consumption",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.W400
                    )
                    Text(text = "396")
                }
                Row {
                    Text(
                        text = "Amount HT",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.W400
                    )
                    Text(text = "1342.96 DA")
                }
                Row {
                    Text(
                        text = "TVA",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.W400
                    )
                    Text(text = "345.96 DA")
                }
            }
        }
        TextTitleMedium(
            textId = R.string.gaz,
        )
        Surface(
//            color = Color(233, 192, 155, 205),
            shape = shape,
            tonalElevation = smallDp,
        ) {
            Column(
                modifier = Modifier.padding(largeDp),
                verticalArrangement = verticalSpacedBy(smallDp)
            ) {
                Row {
                    Text(
                        text = "Consumption",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.W400
                    )
                    Text(text = "196")
                }
                Row {
                    Text(
                        text = "Amount HT",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.W400
                    )
                    Text(text = "642.96 DA")
                }
                Row {
                    Text(
                        text = "TVA",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.W400
                    )
                    Text(text = "275.96 DA")
                }
            }
        }
        TextTitleMedium(
            textId = R.string.total,
        )
        Surface(
//            color = Color(233, 192, 155, 255),
            shape = shape,
            tonalElevation = smallDp,
//            shadowElevation = smallDp,
        ) {
            Column(
                modifier = Modifier.padding(largeDp),
                verticalArrangement = verticalSpacedBy(smallDp)
            ) {
                Row {
                    Text(
                        text = "Total Amount HT",
                        modifier = Modifier.weight(1f),
//                        fontWeight = FontWeight.W400
                    )
                    Text(text = "1969.15 DA")
                }
                Row {
                    Text(
                        text = "TVA",
                        modifier = Modifier.weight(1f),
//                        fontWeight = FontWeight.W400
                    )
                    Text(text = "642.96 DA")
                }
                Row {
                    Text(
                        text = "Rights & Tax",
                        modifier = Modifier.weight(1f),
//                        fontWeight = FontWeight.W400
                    )
                    Text(text = "200.00 DA")
                }
                Row {
                    Text(
                        text = "State Support",
                        modifier = Modifier.weight(1f),
//                        fontWeight = FontWeight.W400
                    )
                    Text(text = "442.96 DA")
                }
                Row {
                    Text(
                        text = "Net Payable TTC",
                        modifier = Modifier.weight(1f),
//                        fontWeight = FontWeight.W400
                    )
                    Text(text = "2275.96 DA")
                }
                Row {
                    Text(
                        text = "Timbre",
                        modifier = Modifier.weight(1f),
//                        fontWeight = FontWeight.W400
                    )
                    Text(text = "23.00 DA")
                }
                Row {
                    Text(
                        text = "Total to Pay",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = "2298.96 DA", fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Row(horizontalArrangement = horizontalSpacedBy()) {
            Button(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text("Download as Pdf")
            }
            OutlinedIconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    modifier = Modifier.rotate(45f),
                    contentDescription = stringResource(R.string.close)
                )
            }
        }
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
                            contentPadding = PaddingValues(largeDp)
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

private enum class HomeScreenContentType {
    SYNC_LOADING, BILL_PREVIEW_ITEMS, BOTTOM_INDICATORS_PADDING
}
