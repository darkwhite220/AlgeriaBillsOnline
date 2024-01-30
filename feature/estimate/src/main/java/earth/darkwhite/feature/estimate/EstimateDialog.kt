package earth.darkwhite.feature.estimate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.TextWithEmphasise
import earth.core.designsystem.components.largeCorner
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.components.verticalSpacedBy
import earth.core.designsystem.icon.AppIcons
import earth.feature.estimate.R

@Composable
fun EstimateDetailDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(largeCorner)),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = verticalSpacedBy(),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(mediumDp)
            ) {
                TextTitleLarge(
                    textId = R.string.estimate_details,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = largeDp)
                )
                Text(
                    text = stringResource(R.string.select_menage_details).trimMargin().trimIndent()
                )
                Text(
                    text = stringResource(R.string.select_state_support_details).trimMargin()
                        .trimIndent()
                )
                
                Text(text = stringResource(R.string.select_elect_pmd_details))
                Image(
                    painter = painterResource(id = AppIcons.ElectricityPMD),
                    modifier = modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = stringResource(id = R.string.elect_pmd_image)
                )
                Text(text = stringResource(R.string.select_gas_pcs_details))
                Image(
                    painter = painterResource(id = AppIcons.GasPcs),
                    modifier = modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = stringResource(id = R.string.gas_pcs_image)
                )
                Text(text = stringResource(R.string.select_consumption_details))
                TextWithEmphasise(
                    text = stringResource(R.string.select_details_note),
                    style = MaterialTheme.typography.bodySmall
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = earth.core.designsystem.R.string.close))
                    }
                }
            }
        }
    }
}