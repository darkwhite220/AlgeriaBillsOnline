package com.darkwhite.feature.createaccount.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier


@Composable
fun TextFieldDescription(description: String? = null) {
    description?.let {
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(alpha = .6f)
        ) {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}