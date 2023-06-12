import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeskeleton.ui.theme.Purple200
import com.example.mycomposeskeleton.ui.theme.Purple500
import com.example.mycomposeskeleton.ui.theme.Purple700

@Composable
fun TextClickable(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 24.dp, bottom = 16.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text= title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Icon(
            Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Color.White
        )
    }
}