package earth.feature.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import earth.core.database.Bill
import earth.core.designsystem.components.BillBottomSheetUtil
import earth.core.designsystem.components.ConsumptionInfoDisplay
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.verticalSpacedBy
import earth.feature.home.HomeEvent
import earth.feature.home.R


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BillBottomSheet(
    sheetState: SheetState,
    bill: Bill,
    onHomeEvent: (HomeEvent) -> Unit,
) {
    val context = LocalContext.current
    val billBottomSheetUtil = remember { BillBottomSheetUtil(context, bill) }
    
    ModalBottomSheet(
        onDismissRequest = { onHomeEvent(HomeEvent.OnBillCloseClick) },
        sheetState = sheetState,
        shape = BottomSheetDefaults.HiddenShape
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TextTitleLarge(
                textId = R.string.bill_details,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold
            )
            Column(
                modifier = Modifier.padding(largeDp),
                verticalArrangement = verticalSpacedBy()
            ) {
                
                billBottomSheetUtil.billHeader.forEach {
                    Text(text = it)
                }
                
                ConsumptionInfoDisplay(billBottomSheetUtil = billBottomSheetUtil)
                
                Row(horizontalArrangement = horizontalSpacedBy()) {
                    Button(
                        onClick = { onHomeEvent(HomeEvent.OnBillDownloadClick(bill)) },
                        enabled = bill.pdfByteArray != null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.download_as_pdf))
                    }
                    FilledIconButton(
                        onClick = { onHomeEvent(HomeEvent.OnBillCloseClick) }
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
    }
}
