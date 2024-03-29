package earth.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.MyButton
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.TextDisplaySmall
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.TextWithEmphasise
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.feature.home.HomeEvent
import earth.feature.home.R


@Composable
fun AddAccountPage(onHomeEvent: (HomeEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = largeDp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextDisplaySmall(textId = R.string.track_more_bills, textAlign = TextAlign.Center)
        MyHeightSpacer(height = 40.dp)
        
        TextTitleLarge(
            textId = R.string.need_to_track_bills_for_a_new_place,
            modifier = Modifier.fillMaxWidth()
        )
        MyHeightSpacer(mediumDp)
        TextWithEmphasise(
            modifier = Modifier.fillMaxWidth(),
            textId = R.string.create_an_account_to_track_the_bills
        )
        MyHeightSpacer(largeDp)
        MyButton(
            textId = R.string.create_account,
            onClick = { onHomeEvent(HomeEvent.OnCreateAccountClick) }
        )
        MyHeightSpacer(largeDp)
        
        TextTitleLarge(textId = R.string.have_an_account)
        MyHeightSpacer(mediumDp)
        TextWithEmphasise(
            modifier = Modifier.fillMaxWidth(),
            textId = R.string.sign_in_to_continue
        )
        MyHeightSpacer(largeDp)
        MyButton(
            textId = R.string.sign_in,
            onClick = { onHomeEvent(HomeEvent.OnSignInClick) }
        )
        
        // Bottom indicators padding
        MyHeightSpacer(largeDp)
    }
}
