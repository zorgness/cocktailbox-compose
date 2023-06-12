import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mycomposeskeleton.network.dto.Drink
import com.example.mycomposeskeleton.ui.theme.Purple200
import com.example.mycomposeskeleton.ui.theme.Purple500
import com.example.mycomposeskeleton.ui.theme.Purple700

@Composable
fun ItemDrink(
    item: Drink,
    isFavorite: Boolean = false,
    onItemClicked: (Long) -> Unit,
    onFavoriteClicked: (Long) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable { onItemClicked(item.idDrink.toLong()) }
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .width(140.dp),
            verticalArrangement = Arrangement.Center

        ) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.strDrinkThumb)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale= ContentScale.FillWidth,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                )

                Icon(
                    Icons.Outlined.Favorite,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                        .clickable {
                            onFavoriteClicked(item.idDrink.toLong())
                        },
                    tint = if(isFavorite) Purple500 else Color.White
                )
            }


            Text(
                text = item.strDrink,
                modifier = Modifier
                    .padding(8.dp),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }
    }
}
