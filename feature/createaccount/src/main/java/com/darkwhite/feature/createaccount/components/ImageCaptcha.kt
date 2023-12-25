package com.darkwhite.feature.createaccount.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.darkwhite.feature.createaccount.uistate.CaptchaUiState
import earth.core.designsystem.components.MyCircularProgressBar
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.largeCorner
import earth.core.designsystem.components.largeDp
import earth.feature.createaccount.R


@Composable
fun CaptchaUi(
    textFieldType: MyTextFieldTypes,
    captchaUiState: CaptchaUiState
) {
    if (textFieldType == MyTextFieldTypes.CAPTCHA) {
        
        val shape = RoundedCornerShape(largeCorner)
        val modifier = Modifier.size(width = 280.dp, height = 84.dp)
        
        Box(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = shape
                )
                .clip(shape)
        ) {
            
            when (captchaUiState) {
                CaptchaUiState.Loading -> {
                    MyCircularProgressBar(modifier = Modifier.align(Alignment.Center))
                }
                is CaptchaUiState.Success -> {
                    ImageCaptcha(modifier = modifier, bitmap = captchaUiState.captchaBitmap)
                }
                is CaptchaUiState.Failed -> {
                    ImageCaptcha(modifier = modifier, bitmap = null)
                    // TODO Captcha error implement
                    println("FAILED, ${captchaUiState.exception}")
                }
            }
        }
        
        MyHeightSpacer(largeDp)
    }
}

@Composable
private fun ImageCaptcha(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?
) {
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            modifier = modifier,
            contentDescription = null
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.broken_image),
            modifier = modifier,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageCaptchaPreview() {
    val shape = RoundedCornerShape(largeCorner)
    ImageCaptcha(
        modifier = Modifier
            .size(width = 280.dp, height = 84.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .clip(shape), bitmap = null
    )
}