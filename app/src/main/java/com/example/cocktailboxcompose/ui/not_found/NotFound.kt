import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NotFoundScreen() {
    NotFoundContent()
}

@Composable
fun NotFoundContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            text= "Sorry Not Found",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White
        )
    }
}