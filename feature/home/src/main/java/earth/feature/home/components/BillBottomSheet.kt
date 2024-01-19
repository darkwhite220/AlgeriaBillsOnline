package earth.feature.home.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import earth.core.database.Bill
import earth.core.designsystem.Util.getNumberSuffix
import earth.core.designsystem.Util.printDate
import earth.core.designsystem.Util.toPrice
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.TextTitleMedium
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.smallDp
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
    val shape = RoundedCornerShape(largeDp)
    
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
                
                TextTitleMedium(
                    textId = R.string.electricity,
                )
                Surface(
//            color = Color(155, 186, 233, 205),
                    shape = shape,
                    tonalElevation = largeDp,
                ) {
                    Column(
                        modifier = Modifier.padding(largeDp),
                        verticalArrangement = verticalSpacedBy(smallDp)
                    ) {
                        billBottomSheetUtil.electricity.forEach {
                            Row {
                                TextValueName(it.valueName)
                                TextValue(it.value)
                            }
                        }
                    }
                }
                
                TextTitleMedium(
                    textId = R.string.gaz,
                )
                Surface(
//            color = Color(233, 192, 155, 205),
                    shape = shape,
                    tonalElevation = largeDp,
                ) {
                    Column(
                        modifier = Modifier.padding(largeDp),
                        verticalArrangement = verticalSpacedBy(smallDp)
                    ) {
                        billBottomSheetUtil.gaz.forEach {
                            Row {
                                TextValueName(it.valueName)
                                TextValue(it.value)
                            }
                        }
                    }
                }
                
                TextTitleMedium(
                    textId = R.string.total,
                )
                Surface(
//            color = Color(233, 192, 155, 255),
                    shape = shape,
                    tonalElevation = largeDp,
                ) {
                    Column(
                        modifier = Modifier.padding(largeDp),
                        verticalArrangement = verticalSpacedBy(smallDp)
                    ) {
                        billBottomSheetUtil.total.forEachIndexed { index, item ->
                            Row {
                                TextValueName(item.valueName)
                                TextValue(
                                    text = item.value,
                                    style = if (index + 1 == billBottomSheetUtil.total.size)
                                        MaterialTheme.typography.titleLarge
                                    else MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                
                Row(horizontalArrangement = horizontalSpacedBy()) {
                    Button(
                        onClick = { onHomeEvent(HomeEvent.OnBillDownloadClick) },
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

@Composable
private fun RowScope.TextValueName(@StringRes textId: Int) {
    Text(
        text = stringResource(id = textId),
        modifier = Modifier.weight(1f),
    )
}

@Composable
private fun TextValue(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .74f),
    )
}

private class BillBottomSheetUtil(context: Context, bill: Bill) {
    
    data class BillStringLine(@StringRes val valueName: Int, val value: String)
    
    val billHeader = listOf(
        context.getString(R.string.bill_n, bill.billNumber),
        context.getString(R.string.Period) + " " + context.getString(
            R.string.bill_trimester_date,
            bill.trimester,
            context.getString(bill.trimester.toInt().getNumberSuffix()),
            context.getString(R.string.trimester),
            bill.year,
            bill.date.printDate()
        )
    )
    val electricity = listOf(
        BillStringLine(
            R.string.consumption,
            context.getString(
                R.string.electricity_consumption_value,
                bill.electConsumption
            )
        ),
        BillStringLine(
            R.string.amount_ht,
            context.getString(
                R.string.price_da,
                bill.electConsumptionCost,
            )
        ),
        BillStringLine(
            R.string.vat,
            context.getString(
                R.string.price_da,
                bill.electricityTva,
            )
        )
    )
    val gaz = listOf(
        BillStringLine(
            R.string.consumption,
            context.getString(
                R.string.gaz_consumption_value,
                bill.gazConsumption
            )
        ),
        BillStringLine(
            R.string.amount_ht,
            context.getString(
                R.string.price_da,
                bill.gazConsumptionCost,
            )
        ),
        BillStringLine(
            R.string.vat,
            context.getString(
                R.string.price_da,
                bill.gazTva,
            )
        )
    )
    val total = listOf(
        BillStringLine(
            R.string.rights_and_taxes,
            context.getString(
                R.string.price_da,
                bill.rightsAndTaxes.toPrice(),
            )
        ),
        BillStringLine(
            R.string.total_amount_ht,
            context.getString(
                R.string.electricity_consumption_value,
                bill.totalHT
            )
        ),
        BillStringLine(
            R.string.total_vat,
            context.getString(
                R.string.price_da,
                bill.totalTva,
            )
        ),
        BillStringLine(
            R.string.state_support,
            context.getString(
                R.string.price_da,
                bill.stateSupport,
            )
        ),
        BillStringLine(
            R.string.net_payable_ttc,
            context.getString(
                R.string.price_da,
                bill.totalTTCNoTimbre,
            )
        ),
        BillStringLine(
            R.string.timbre,
            context.getString(
                R.string.price_da,
                bill.timbre,
            )
        ),
        BillStringLine(
            R.string.total_to_pay,
            context.getString(
                R.string.price_da,
                bill.totalTTC,
            )
        )
    )
}
