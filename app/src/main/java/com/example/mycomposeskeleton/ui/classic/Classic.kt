package com.example.mycomposeskeleton.ui.classic

import DrinkListContent
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mycomposeskeleton.R
import com.example.mycomposeskeleton.utils.Screen


@Composable
fun ClassicScreen(
    navController: NavHostController,
    viewModel: ClassicViewModel
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
        list = classicSummaryList,
        handleItemClicked = { drinkId->
            navController.navigate(Screen.Details.route + "/$drinkId" )
        },
        handleSearch = { viewModel.updateSearchText(it) },
        handleFavoriteClicked = {}
    )
}
