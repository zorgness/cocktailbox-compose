import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mycomposeskeleton.network.dto.Drink

@Composable
fun CustomLazyRow(
    title: String,
    list: List<Drink>,
    checkIsFavorite: (Long) -> Boolean,
    handleTitleClicked: () -> Unit,
    handleItemClicked: (Long) -> Unit,
    handleFavoriteClicked: (Long) -> Unit,
) {


    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        TextClickable(
            title = title,
            onClick = { handleTitleClicked() }
        )
        LazyRow(
            modifier = Modifier.padding(start = 24.dp)
        ) {
            items(list.size) { index ->
                var isFavorite by remember {
                    mutableStateOf(checkIsFavorite(list[index].idDrink.toLong()))
                }
                ItemDrink(
                    item = list[index],
                    isFavorite = isFavorite,
                    onItemClicked = {
                        handleItemClicked(list[index].idDrink.toLong())
                    },
                    onFavoriteClicked = {
                        handleFavoriteClicked(list[index].idDrink.toLong())
                        isFavorite = !isFavorite
                    }
                )
            }
        }
    }

}
