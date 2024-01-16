package earth.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp


@Composable
fun MyCircularProgressBar(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(20.dp),
        strokeWidth = 2.dp,
        strokeCap = StrokeCap.Square
    )
}

@Composable
fun MyLinearProgressBar() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
    )
}
