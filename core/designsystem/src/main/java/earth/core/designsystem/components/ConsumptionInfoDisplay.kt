package earth.core.designsystem.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import earth.core.database.Bill
import earth.core.designsystem.R
import earth.core.designsystem.utils.Util.getNumberSuffix
import earth.core.designsystem.utils.Util.printDate
import earth.core.designsystem.utils.Util.toPrice


@Composable
fun ConsumptionInfoDisplay(
    billBottomSheetUtil: BillBottomSheetUtil
) {
    TextTitleMedium(
        textId = R.string.electricity,
    )
    Surface(
        shape = cardShape,
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
        shape = cardShape,
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
        shape = cardShape,
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

class BillBottomSheetUtil(context: Context, bill: Bill) {
    
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
            R.string.total_amount_ht,
            context.getString(
                R.string.price_da,
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
            R.string.rights_and_taxes,
            context.getString(
                R.string.price_da,
                bill.rightsAndTaxes.toPrice(),
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
                bill.timbre.toPrice(),
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
