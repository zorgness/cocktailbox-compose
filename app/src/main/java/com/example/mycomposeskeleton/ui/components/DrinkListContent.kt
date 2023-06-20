import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeskeleton.R
import com.example.mycomposeskeleton.myanimation.addBrushEffect
import com.example.mycomposeskeleton.network.dto.Drink

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrinkListContent(
    title: String,
    searchText: String,
    isPbVisible: Boolean,
    checkIsFavorite: (Long) -> Boolean,
    list: List<Drink>,
    handleItemClicked: (Long) -> Unit,
    handleSearch: (String) -> Unit,
    handleFavoriteClicked: (Long) -> Unit
) {
    var isSearchBarVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp),
            style = TextStyle(brush = addBrushEffect(10000))
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            SearchHeader(
                context,
                onBtnSearchClicked = { isSearchBarVisible = !isSearchBarVisible },
            )
            AnimatedVisibility(
                visible = isSearchBarVisible
            ) {
                SearchBar(
                    context,
                    searchText,
                    handleChange = { handleSearch(it) }
                )
            }


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp),
                content = {

                    items(list.size) { index ->
                        val isFavorite by remember {
                            mutableStateOf(
                                checkIsFavorite(list[index].idDrink.toLong())
                            )
                        }
                        ItemDrink(
                            item = list[index],
                            isFavorite = isFavorite,
                            onItemClicked = { handleItemClicked(it) },
                            onFavoriteClicked = { handleFavoriteClicked(it) },
                        )
                    }
                }
            )
        }
        AnimatedVisibility(
            visible = isPbVisible,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun SearchHeader(
    context: Context,
    onBtnSearchClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp),
            onClick = { onBtnSearchClicked() }
        ) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier
                    .size(ButtonDefaults.IconSize)

            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = context.getString(R.string.search))

        }

    }
}

@Composable
fun SearchBar(
    context: Context,
    searchText: String,
    handleChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 6.dp)
                .focusRequester(focusRequester),
            value = searchText,
            onValueChange = { handleChange(it) },
            placeholder = { Text(text = context.getString(R.string.enter_your_research)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.Gray,
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
}
