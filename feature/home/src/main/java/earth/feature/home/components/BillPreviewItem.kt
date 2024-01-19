package earth.feature.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import earth.core.database.BillPreview
import earth.core.designsystem.Util.getNumberSuffix
import earth.core.designsystem.Util.printDate
import earth.core.designsystem.Util.toPrice
import earth.core.designsystem.components.TextWithEmphasise
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.smallDp
import earth.core.designsystem.components.verticalSpacedBy
import earth.core.designsystem.icon.AppIcons
import earth.feature.home.HomeEvent
import earth.feature.home.R


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BillPreviewItem(
    onHomeEvent: (HomeEvent) -> Unit,
    item: BillPreview,
    consumptionLevel: ConsumptionLevel
) {
    ElevatedCard(
        onClick = {
            onHomeEvent(HomeEvent.OnBillPreviewClick(item.billNumber))
        },
//        colors = CardDefaults.cardColors(
//            containerColor = consumptionLevel.backgroundColor
//        )
    ) {
        Row(
            modifier = Modifier
                .padding(largeDp)
                .fillMaxWidth(),
            horizontalArrangement = horizontalSpacedBy(largeDp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BillPreviewConsumptionIcon(consumptionLevel)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = verticalSpacedBy(smallDp)
            ) {
                TextWithEmphasise(
                    text = stringResource(R.string.bill_n, item.billNumber),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                )
                val date = stringResource(
                    R.string.bill_trimester_date,
                    item.trimester,
                    stringResource(item.trimester.toInt().getNumberSuffix()),
                    stringResource(R.string.trimester),
                    item.year,
                    item.date.printDate()
                )
                TextWithEmphasise(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )
            }
            
            TextWithEmphasise(
                text = stringResource(R.string.price_da, item.totalTTC.toPrice()),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun BillPreviewConsumptionIcon(consumptionLevel: ConsumptionLevel) {
    if (consumptionLevel.iconTint != null) {
        Icon(
            painter = painterResource(consumptionLevel.icon),
            tint = consumptionLevel.iconTint,
            modifier = consumptionLevel.modifier,
            contentDescription = stringResource(R.string.consumption_level),
        )
    } else {
        Icon(
            painter = painterResource(consumptionLevel.icon),
            modifier = consumptionLevel.modifier,
            contentDescription = stringResource(R.string.consumption_level),
        )
    }
}

@Composable
fun consumptionLevel(
    billPreview: List<BillPreview>,
    index: Int,
    item: BillPreview,
): ConsumptionLevel = when {
    index + 1 == billPreview.size -> {
        ConsumptionLevel.EQUAL
    }
    billPreview[index + 1].totalTTC < item.totalTTC -> {
        ConsumptionLevel.UP
    }
    billPreview[index + 1].totalTTC > item.totalTTC -> {
        ConsumptionLevel.DOWN
    }
    else -> {
        ConsumptionLevel.EQUAL
    }
}

enum class ConsumptionLevel(
    @DrawableRes val icon: Int,
    val iconTint: Color?,
    val backgroundColor: Color,
    val modifier: Modifier
) {
    EQUAL(
        icon = AppIcons.ConsumptionStable,
        iconTint = null,
        backgroundColor = Color.Unspecified,
        modifier = Modifier.width(40.dp)
    ),
    UP(
        icon = AppIcons.ConsumptionUp,
        iconTint = Color(197, 0, 0, 255),
        //Color(147, 56, 56, 255), // MaterialTheme.colorScheme.error
        backgroundColor = Color(255, 235, 235, 221),
        modifier = Modifier
    ),
    DOWN(
        icon = AppIcons.ConsumptionUp,
        iconTint = Color(56, 147, 130, 255),
        backgroundColor = Color(228, 241, 234, 221),
        modifier = Modifier.graphicsLayer { rotationX = 180f }
    ),
}
