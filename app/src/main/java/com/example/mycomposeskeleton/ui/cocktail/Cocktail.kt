package com.example.mycomposeskeleton.ui.cocktail

import DrinkListContent
import ItemDrink
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mycomposeskeleton.ui.theme.MyComposeSkeletonTheme
import com.example.mycomposeskeleton.R
import com.example.mycomposeskeleton.network.dto.Drink
import com.example.mycomposeskeleton.ui.favorite.FavoriteViewModel
import com.example.mycomposeskeleton.utils.Screen


@Composable
fun CocktailScreen(
    navController: NavHostController,
    viewModel: CocktailViewModel,
    sharedViewModel: FavoriteViewModel
) {
    val searchText by viewModel.searchStrStateFlow.collectAsState()
    val isPbVisible by viewModel.isPbVisibleStateFlow.collectAsState()
    val cocktailSummaryList by viewModel.cocktailSummaryListStateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.messageSharedFlow.collect { state ->
            when (state) {
                CocktailViewModel.CocktailState.ERROR_SERVER -> R.string.error_server
                CocktailViewModel.CocktailState.ERROR_CONNECTION -> R.string.error_connection
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.isListReadySharedFlow.collect { isReady ->
            if (isReady)
                viewModel.fetchCocktailsToShow()
        }
    }

    DrinkListContent(
        title = context.getString(R.string.cocktails),
        searchText = searchText,
        isPbVisible = isPbVisible,
        checkIsFavorite = { drinkId ->
            sharedViewModel.checkIsFavorite(drinkId)
        },
        list = cocktailSummaryList,
        handleItemClicked = { drinkId->
            navController.navigate(Screen.Details.route + "/$drinkId" )
        },
        handleSearch = { viewModel.updateSearchText(it) },
        handleFavoriteClicked = { drinkId ->
            sharedViewModel.setFavorite(drinkId) { isSet ->
                if(isSet)
                    viewModel.fetchCocktailsToShow()
            }
        }
    )
}
