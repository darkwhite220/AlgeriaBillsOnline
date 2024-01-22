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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import earth.core.designsystem.R
import earth.core.designsystem.components.largeCorner
import earth.core.designsystem.icon.AppIcons

@Preview(showSystemUi = true)
@Composable
fun ReferenceDetailDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = Modifier.clip(RoundedCornerShape(largeCorner))) {
            Image(
                painter = painterResource(id = AppIcons.BillReference),
                modifier = modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                contentDescription = stringResource(id = R.string.reference_detail_image)
            )
        }
    }
}