package com.example.cocktailboxcompose.ui.classic

import DrinkListContent
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.cocktailboxcompose.ui.favorite.FavoriteViewModel
import com.example.cocktailboxcompose.utils.Screen
import com.example.mycomposeskeleton.R


@Composable
fun ClassicScreen(
    navController: NavHostController,
    viewModel: ClassicViewModel,
    sharedViewModel: FavoriteViewModel
) {
    val searchText by viewModel.searchStrStateFlow.collectAsState()
    val isPbVisible by viewModel.isPbVisibleStateFlow.collectAsState()
    val classicSummaryList by viewModel.classicSummaryListStateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.messageSharedFlow.collect { state ->
            when (state) {
                ClassicViewModel.ClassicState.ERROR_SERVER -> R.string.error_server
                ClassicViewModel.ClassicState.ERROR_CONNECTION -> R.string.error_connection
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.isListReadySharedFlow.collect { isReady ->
            if (isReady)
                viewModel.fetchDrinksToShow()
        }
    }

    DrinkListContent(
        title = context.getString(R.string.classics),
        searchText = searchText,
        isPbVisible = isPbVisible,
        checkIsFavorite = { drinkId ->
            sharedViewModel.checkIsFavorite(drinkId)
        },
        list = classicSummaryList,
        handleItemClicked = { drinkId->
            navController.navigate(Screen.Details.route + "/$drinkId" )
        },
        handleSearch = { viewModel.updateSearchText(it) },
        handleFavoriteClicked = {drinkId ->
            sharedViewModel.setFavorite(drinkId) {
                viewModel.fetchDrinksToShow()
            }
        }
    )
}
