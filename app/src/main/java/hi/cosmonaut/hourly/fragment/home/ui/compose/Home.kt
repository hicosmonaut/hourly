package hi.cosmonaut.hourly.fragment.home.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

object Home {

    @Composable
    fun NoticeCard(
        iconPainter: Painter,
        text: String,
        containerColor: Color,
        contentColor: Color,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            backgroundColor = containerColor
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = contentColor
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = contentColor
                )
            }
        }
    }
}