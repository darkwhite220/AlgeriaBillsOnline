package earth.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun MyLottieAnimation(
    modifier: Modifier = Modifier,
    fileName: String,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(fileName))
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = 3,
        reverseOnRepeat = true
    )
}