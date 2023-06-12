package com.example.mycomposeskeleton.ui.non_alcoolic
import DrinkListContent
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mycomposeskeleton.R
import com.example.mycomposeskeleton.utils.Screen


@Composable
fun NonAlcoolicScreen(
    navController: NavHostController,
    viewModel: NonAlcoolicViewModel
) {
    val searchText by viewModel.searchStrStateFlow.collectAsState()
    val isPbVisible by viewModel.isPbVisibleStateFlow.collectAsState()
    val nonAlcoolicSummaryList by viewModel.nonAlcoolicSummaryListStateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.messageSharedFlow.collect { state ->
            when (state) {
                NonAlcoolicViewModel.NonAlcoolicState.ERROR_SERVER -> R.string.error_server
                NonAlcoolicViewModel.NonAlcoolicState.ERROR_CONNECTION -> R.string.error_connection
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
        title = context.getString(R.string.non_alcoolics),
        searchText = searchText,
        isPbVisible = isPbVisible,
        list = nonAlcoolicSummaryList,
        handleItemClicked = { drinkId->
            navController.navigate(Screen.Details.route + "/$drinkId" )
        },
        handleSearch = { viewModel.updateSearchText(it) },
        handleFavoriteClicked = {}
    )
}
