package com.darkwhite.feature.createaccount.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import earth.core.designsystem.components.largeCorner
import earth.feature.createaccount.R


@Composable
fun ReferenceDetailDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = Modifier.clip(RoundedCornerShape(largeCorner))) {
            Image(
                modifier = modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.bill_reference2),
                contentDescription = stringResource(id = R.string.reference_detail_icon)
            )
        }
    }
}