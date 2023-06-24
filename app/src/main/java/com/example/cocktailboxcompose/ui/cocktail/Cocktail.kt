package com.example.cocktailboxcompose.ui.cocktail

import DrinkListContent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mycomposeskeleton.R
import com.example.cocktailboxcompose.ui.favorite.FavoriteViewModel
import com.example.cocktailboxcompose.utils.Screen


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
