package earth.darkwhite.feature.estimate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.database.Bill
import earth.core.database.ElectricityPMD
import earth.core.database.MenageType
import earth.core.database.StateSupport
import earth.core.designsystem.components.BillBottomSheetUtil
import earth.core.designsystem.components.ConsumptionInfoDisplay
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.textfield.EditableUserInput
import earth.core.designsystem.components.textfield.UserInput
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.core.designsystem.components.verticalSpacedBy
import earth.core.designsystem.icon.AppIcons
import earth.feature.estimate.R

@Composable
internal fun EstimateRoute(
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: EstimateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val billEstimate = viewModel.billEstimate
    var showDetailDialog by remember { mutableStateOf(false) }
    
    EstimateScreen(
        uiState = uiState,
        billEstimate = billEstimate,
        onEstimateEvent = viewModel::onEvent,
        onSettingsClick = onSettingsClick,
        onBackClick = onBackClick,
        onShowDetailDialogClick = { showDetailDialog = true },
    )
    
    if (showDetailDialog) {
        EstimateDetailDialog { showDetailDialog = false }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
private fun EstimateScreen(
    uiState: EstimateUiState = EstimateUiState(),
    billEstimate: Bill = Bill(),
    onEstimateEvent: (EstimateEvent) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onShowDetailDialogClick: () -> Unit = {},
) {
    val backdropScaffoldState =
        rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
    
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = {
            CenteredTopAppBar(
                titleId = R.string.estimate,
                onBackClick = onBackClick,
                actionIconId = AppIcons.Info,
                onActionClick = onShowDetailDialogClick
            )
        },
        backLayerBackgroundColor = MaterialTheme.colorScheme.background,
        backLayerContent = {
            val focusManager = LocalFocusManager.current
            val focusRequester = remember { FocusRequester() }
            Column(
                modifier = Modifier
                    .padding(horizontal = largeDp)
                    .padding(bottom = largeDp),
                verticalArrangement = verticalSpacedBy()
            ) {
                uiState.apply {
                    UserInput(
                        text = menageType.text(),
                        vectorImageId = null,
                        onClick = { onEstimateEvent(EstimateEvent.OnMenageTypeClick) }
                    )
                    UserInput(
                        text = stateSupport.text(),
                        onClick = { onEstimateEvent(EstimateEvent.OnStateSupportClick) }
                    )
                    Row(horizontalArrangement = horizontalSpacedBy()) {
                        UserInput(
                            modifier = Modifier.weight(1f),
                            text = electricityPMD.text(),
                            onClick = { onEstimateEvent(EstimateEvent.OnElectricityDPMClick) }
                        )
                        EditableUserInput(
                            modifier = Modifier.weight(1f),
                            text = gasPcs,
                            hint = stringResource(id = R.string.gas_pcs_hint),
                            caption = stringResource(id = R.string.gas_pcs),
                            focusRequester = focusRequester,
                            imeAction = ImeAction.Next,
                            onInputChanged = { onEstimateEvent(EstimateEvent.OnGasPCSClick(it)) },
                            onImeAction = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    }
                    EditableUserInput(
                        text = electricityConsumption,
                        hint = stringResource(R.string.electricity_consumption),
                        caption = stringResource(id = R.string.electricity_consumption),
                        vectorImageId = AppIcons.LightningBolt,
                        focusRequester = focusRequester,
                        imeAction = ImeAction.Next,
                        onInputChanged = {
                            onEstimateEvent(EstimateEvent.OnElectricityConsumptionChange(it))
                        },
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    )
                    EditableUserInput(
                        text = gasConsumption,
                        hint = stringResource(R.string.gaz_consumption),
                        caption = stringResource(id = R.string.gaz_consumption),
                        vectorImageId = AppIcons.Fire,
                        focusRequester = focusRequester,
                        imeAction = ImeAction.Done,
                        onInputChanged = { onEstimateEvent(EstimateEvent.OnGasConsumptionChange(it)) },
                        onImeAction = {
                            focusManager.clearFocus(true)
                        },
                    )
                }
            }
        },
        frontLayerScrimColor = Color.Unspecified,
        frontLayerContent = {
            Surface(tonalElevation = BottomAppBarDefaults.ContainerElevation) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(largeDp),
                    verticalArrangement = verticalSpacedBy()
                ) {
                    val context = LocalContext.current
                    val billBottomSheetUtil =
                        remember(billEstimate) { BillBottomSheetUtil(context, billEstimate) }
                    ConsumptionInfoDisplay(billBottomSheetUtil = billBottomSheetUtil)
                }
            }
        },
    )
}

@Composable
private fun MenageType.text() = when (this) {
    MenageType.M -> stringResource(id = R.string.household_city)
    MenageType.M_OUT_CITY -> stringResource(id = R.string.household_out_city)
    MenageType.NM -> stringResource(id = R.string.none_household_city)
    MenageType.NM_OUT_CITY -> stringResource(id = R.string.none_household_out_city)
}

@Composable
private fun StateSupport.text() = when (this) {
    StateSupport.NONE -> stringResource(id = R.string.state_support_none)
    StateSupport.HIGH_PLATEAU -> stringResource(id = R.string.state_support_high_plateau)
    StateSupport.SOUTH_NONE_AGRICULTURE -> stringResource(id = R.string.state_support_sought_none_agriculture)
    StateSupport.SOUTH_AGRICULTURE -> stringResource(id = R.string.state_support_sought_agriculture)
}

@Composable
private fun ElectricityPMD.text() = when (this) {
    ElectricityPMD.LOW_MONO_PHASE -> stringResource(id = R.string.electricity_dpm_4)
    ElectricityPMD.MEDIUM_MONO_PHASE -> stringResource(id = R.string.electricity_dpm_6)
    ElectricityPMD.HIGH_MONO_PHASE -> stringResource(id = R.string.electricity_dpm_12)
    ElectricityPMD.LOW_TRI_PHASE -> stringResource(id = R.string.electricity_dpm_20)
    ElectricityPMD.MEDIUM_TRI_PHASE -> stringResource(id = R.string.electricity_dpm_40)
    ElectricityPMD.HIGH_TRI_PHASE -> stringResource(id = R.string.electricity_dpm_60)
    ElectricityPMD.VERY_HIGH_TRI_PHASE -> stringResource(id = R.string.electricity_dpm_80)
}