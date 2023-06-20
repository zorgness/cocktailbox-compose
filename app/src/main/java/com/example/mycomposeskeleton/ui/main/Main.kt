package com.example.mycomposeskeleton.ui.main

import CustomLazyRow
import ItemDrink
import TextClickable
import android.content.Context
import android.widget.ScrollView
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mycomposeskeleton.R
import com.example.mycomposeskeleton.network.dto.Drink
import com.example.mycomposeskeleton.ui.cocktail.CocktailViewModel
import com.example.mycomposeskeleton.ui.favorite.FavoriteViewModel
import com.example.mycomposeskeleton.ui.theme.MyComposeSkeletonTheme
import com.example.mycomposeskeleton.ui.theme.Purple200
import com.example.mycomposeskeleton.ui.theme.Purple500
import com.example.mycomposeskeleton.ui.theme.Purple700
import com.example.mycomposeskeleton.utils.Screen
import org.intellij.lang.annotations.JdkConstants

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    sharedViewModel: FavoriteViewModel
) {

    val cocktailList by viewModel.cocktailSummaryListStateFlow.collectAsState()
    val ordinaryList by viewModel.ordinarySummaryListStateFlow.collectAsState()
    val nonAlcoolicList by viewModel.nonAlcoolicSummaryListStateFlow.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.messageSharedFlow.collect { state ->
            when (state) {
                MainViewModel.MainState.ERROR_SERVER -> R.string.error_server
                MainViewModel.MainState.ERROR_CONNECTION -> R.string.error_connection
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(true) {
        sharedViewModel.messageSharedFlow.collect { state ->
            when (state) {
                FavoriteViewModel.FavoriteState.ERROR_SERVER -> R.string.error_server
                FavoriteViewModel.FavoriteState.ERROR_CONNECTION -> R.string.error_connection
                FavoriteViewModel.FavoriteState.TOKEN_EXPIRED -> R.string.token_expired
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(true) {
        with(viewModel) {
            fetchCocktailsAll()
            fetchOrdinariesAll()
            fetchNonAlcoolicsAll()
        }
    }

    LaunchedEffect(true) {
        viewModel.goToLoginSharedFlow.collect {
            navController.navigate(it.route) {
                popUpTo(Screen.Main.route) {
                    inclusive = true
                }
            }
        }
    }


    MainContent(
        context = context,
        cocktailList= cocktailList,
        ordinaryList= ordinaryList,
        nonAlcoolicList = nonAlcoolicList,
        checkIsFavorite = { sharedViewModel.checkIsFavorite(it) },
        handleLogout = { viewModel.logout() },
        handleItemClicked = { drinkId ->
            navController.navigate(Screen.Details.route + "/$drinkId" )
        },
        handleFavoriteClicked = {
            sharedViewModel.setFavorite(it) {setFavorite ->
                if(setFavorite)
                    with(viewModel) {
                        fetchCocktailsAll()
                        fetchOrdinariesAll()
                        fetchNonAlcoolicsAll()
                    }
            }


        },
        goToScreen = {
            navController.navigate(it)
        }
    )
}

@Composable
fun MainContent(
    context: Context,
    cocktailList: List<Drink>,
    ordinaryList: List<Drink>,
    nonAlcoolicList: List<Drink>,
    checkIsFavorite: (Long) -> Boolean,
    handleLogout: () -> Unit,
    handleItemClicked: (Long) -> Unit,
    handleFavoriteClicked: (Long) -> Unit,
    goToScreen: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.Black)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            MainHeader(
                context = context,
            ) {
                handleLogout()
            }

            Row(
                modifier = Modifier.height(300.dp)
            ) {
                CustomLazyRow(
                    title = context.getString(R.string.cocktails),
                    list = cocktailList,
                    checkIsFavorite = { checkIsFavorite(it) },
                    handleTitleClicked = { goToScreen(Screen.Cocktail.route) },
                    handleItemClicked = { handleItemClicked(it) },
                    handleFavoriteClicked = { handleFavoriteClicked(it)}
                )
            }

            Row(
                modifier = Modifier.height(300.dp)
            ) {
                CustomLazyRow(
                    title = context.getString(R.string.classics),
                    list = ordinaryList,
                    checkIsFavorite = { checkIsFavorite(it) },
                    handleTitleClicked = { goToScreen(Screen.Classic.route) },
                    handleItemClicked = { handleItemClicked(it) },
                    handleFavoriteClicked = { handleFavoriteClicked(it) }

                )
            }

            Row(
                modifier = Modifier.height(300.dp)
            ) {
                CustomLazyRow(
                    title = context.getString(R.string.non_alcoolics),
                    list = nonAlcoolicList,
                    checkIsFavorite = { checkIsFavorite(it) },
                    handleTitleClicked = { goToScreen(Screen.NonAlcoolic.route) },
                    handleItemClicked = { handleItemClicked(it) },
                    handleFavoriteClicked = { handleFavoriteClicked(it) }
                )
            }

        }
    }
}

@Composable
fun MainHeader(
    context: Context,
    onLogoutIconClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 20.dp,
                top = 40.dp,
                bottom = 60.dp
            ),
        contentAlignment = Alignment.Center

    ) {
        Text(
            text = context.getString(R.string.cocktail_box),
            modifier = Modifier
                .align(Alignment.CenterStart),
            color = Color.White,
            fontSize = 36.sp,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.ExtraBold
        )
        Icon(
            Icons.Outlined.ExitToApp,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterEnd)
                .clickable { onLogoutIconClicked() },
            tint = Purple500
        )
    }
}


