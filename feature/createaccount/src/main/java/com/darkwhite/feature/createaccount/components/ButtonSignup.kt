package com.darkwhite.feature.createaccount.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import earth.core.designsystem.components.MyCircularProgressBar
import earth.core.designsystem.components.largeDp
import earth.feature.createaccount.R


@Preview
@Composable
fun ButtonCreateAccount(
    @StringRes textId: Int = R.string.create_account,
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(.8f),
        enabled = !isLoading
    ) {
        Text(
            text = stringResource(id = textId)
        )
        
        Spacer(modifier = Modifier.width(largeDp))
        
        if (isLoading) {
            MyCircularProgressBar()
        }
    }
}