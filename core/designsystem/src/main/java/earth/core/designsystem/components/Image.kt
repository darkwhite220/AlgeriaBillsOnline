package earth.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun ImageCaptcha(url: String = "") {
    val shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart)
    Image(
        painter = painterResource(id = androidx.core.R.drawable.ic_call_answer),
        modifier = Modifier
            .size(width = 200.dp, height = 60.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .clip(shape),
        contentDescription = null
    )
}